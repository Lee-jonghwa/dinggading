import os
import json
import tempfile
import requests
import numpy as np
import librosa
import soundfile as sf
from io import BytesIO
from celery import Celery
from minio import Minio
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean,cosine
from requests.exceptions import RequestException
from collections import defaultdict
import ssl
from urllib3 import PoolManager

from sklearn.preprocessing import StandardScaler
import scipy.signal

scipy.signal.hann = scipy.signal.windows.hann

# 환경 변수 설정
env_mode = os.getenv("ENV_MODE")
if not env_mode:
    from dotenv import load_dotenv
    load_dotenv()
    env_mode = os.getenv("ENV_MODE", "development")
env_mode = env_mode.strip().lower().replace(".local", "").strip(".")

# MinIO 설정
MINIO_HOST = os.getenv("MINIO_HOST", "localhost")
MINIO_PORT = os.getenv("MINIO_PORT", "9001")
MINIO_ENDPOINT = f"{MINIO_HOST}:{MINIO_PORT}"
MINIO_USERNAME = os.getenv("MINIO_USERNAME")
MINIO_PASSWORD = os.getenv("MINIO_PASSWORD")
MINIO_BUCKET_NAME = os.getenv("_NAME", "original-mp3-audio-bucket")

# 1. SSL 인증서 검증을 무시하는 SSLContext 생성
ssl_context = ssl._create_unverified_context()

# 2. 커스텀 HTTP 클라이언트 생성
http_client = PoolManager(ssl_context=ssl_context)

minio_client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_USERNAME,
    secret_key=MINIO_PASSWORD,
    secure=True,
    http_client=http_client
)

# Celery 설정
celery = Celery(
    "tasks",
    broker=os.getenv("CELERY_BROKER_URL", "redis://localhost:6379/0"),
    backend=os.getenv("CELERY_RESULT_BACKEND", "redis://localhost:6379/0")
)

celery.conf.update(
    worker_concurrency=4,
    task_acks_late=True,
    task_time_limit=120,
    worker_prefetch_multiplier=4
)

def check_minio_connection():
    try:
        return minio_client.bucket_exists(MINIO_BUCKET_NAME)
    except:
        return False

def load_user_audio_from_tests(song_id, instrument_id):
    file_path = f"../../tests/cover_audio/{song_id}/{instrument_id}/CHALLENGE.mp3"
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"사용자 오디오가 존재하지 않습니다: {file_path}")
    y, sr = librosa.load(file_path, sr=None)
    return y.astype(np.float32), sr

def load_json_from_minio(song_id, file_name):
    object_path = f"DB/{song_id}/{file_name}"
    try:
        response = minio_client.get_object(MINIO_BUCKET_NAME, object_path)
        return json.load(BytesIO(response.read()))
    except Exception:
        raise RuntimeError()

def load_npz_from_minio(song_id, instrument_id):
    object_path = f"DB/{song_id}/{instrument_id}/{instrument_id}.npz"
    try:
        response = minio_client.get_object(MINIO_BUCKET_NAME, object_path)
        buffer = BytesIO(response.read())
        data = np.load(buffer)
        if "sr" not in data or data["sr"] is None or "y" not in data:
            raise ValueError(f"npz 파일 오류: {object_path}")
        return data["y"], int(data["sr"])
    except Exception as e:
        raise RuntimeError(f"MinIO에서 npz 로드 실패: {object_path} → {e}")

def align_audio_signals(y_org, y_cov, top_db=30):
    y_trimmed, idx = librosa.effects.trim(y_org, top_db=top_db)
    start_idx, end_idx = idx
    y_org_aligned = y_org[start_idx:end_idx]
    if len(y_cov) >= end_idx:
        y_cov_aligned = y_cov[start_idx:end_idx]
    else:
        y_cov_aligned = y_cov[start_idx:]
    return y_org_aligned, y_cov_aligned

def extract_mfcc_stack(y, sr):
    if y is None or len(y) == 0 or np.allclose(y, 0):
        return np.zeros((39, 1))
    mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13)
    if mfcc.shape[1] == 0:
        return np.zeros((39, 1))
    mfcc = np.nan_to_num(mfcc, nan=-600.0, posinf=-600.0, neginf=-600.0)
    delta = librosa.feature.delta(mfcc)
    delta2 = librosa.feature.delta(mfcc, order=2)
    return np.vstack([mfcc, delta, delta2])

def extract_pyin_pitch(y, sr, hop_length=512):
    if y is None or len(y) == 0 or np.allclose(y, 0):
        n_frames = 1 + len(y) // hop_length
        return np.zeros(n_frames)
    f0, _, _ = librosa.pyin(y, fmin=librosa.note_to_hz('C2'), fmax=librosa.note_to_hz('C7'))
    return np.nan_to_num(f0, nan=0.0)

def extract_onset_strength(y, sr):
    if y is None or len(y) == 0:
        return np.zeros(1)
    return np.nan_to_num(librosa.onset.onset_strength(y=y, sr=sr), nan=0.0)

def safe_onset_detect(y, sr):
    if y is None or len(y) == 0:
        return np.array([0.0])
    onset_pos = librosa.onset.onset_detect(y=y, sr=sr, units='time')
    return onset_pos if len(onset_pos) > 0 else np.array([0.0])

def extract_tempo(y, sr, onset_env, silence_threshold=1e-4):
    if y is None or len(y) == 0 or np.max(np.abs(y)) < silence_threshold:
        return 0.0
    try:
        tempo = float(librosa.beat.tempo(onset_envelope=onset_env, sr=sr, aggregate=np.mean))
        if abs(tempo - 120.0) < 0.1 and np.std(onset_env) < 1e-3:
            return 0.0
        return round(tempo, 2)
    except Exception:
        return 0.0

def evaluate_user_scores(pitch_user, pitch_ref,
                         onset_user_norm, onset_ref_norm,
                         onset_pos_user, onset_pos_ref,
                         tempo_user, tempo_ref,
                         mfcc_stack_user, mfcc_stack_ref, rms_user, rms_ref,
                         sr):

    def dtw_pitch_score(p1, p2):
        distance, _ = fastdtw(p1, p2, dist=euclidean)
        return max(0.0, 100.0 / (1.0 + distance))

    def transpose_score(p1, p2):
        semitone_shift = 12 * np.log2(np.mean(p1[p1 > 0]) / np.mean(p2[p2 > 0]))
        penalty = min(100.0, abs(semitone_shift) * 5)
        return max(0.0, 100.0 - penalty)

    def pitch_chunk_score(p1, p2, chunks=10):
        length = min(len(p1), len(p2))
        chunk_size = length // chunks
        errors = []
        for i in range(chunks):
            s, e = i * chunk_size, (i + 1) * chunk_size
            if e > length:
                break
            diff = np.mean(np.abs(p1[s:e] - p2[s:e]))
            errors.append(diff)
        return max(0.0, 100.0 - np.mean(errors))

    def mfcc_cosine_score(m1, m2):
        return max(0.0, 100.0 * (1 - cosine(np.mean(m1, axis=1), np.mean(m2, axis=1))))

    def mfcc_chunk_rmse(m1, m2, chunks=10):
        length = min(m1.shape[1], m2.shape[1])
        chunk_size = length // chunks
        rmses = []
        for i in range(chunks):
            s, e = i * chunk_size, (i + 1) * chunk_size
            if e > length:
                break
            rmse = np.sqrt(np.mean((m1[:, s:e] - m2[:, s:e]) ** 2))
            rmses.append(rmse)
        avg_rmse = np.mean(rmses)
        return max(0.0, 100.0 / (1 + avg_rmse))

    def onset_match_score(pos1, pos2, delta=0.1):
        matches = sum(any(abs(p - r) < delta for r in pos2) for p in pos1)
        return round(100.0 * matches / len(pos1), 2) if len(pos1) else 0.0

    def onset_chunk_rmse(o1, o2):
        l = min(len(o1), len(o2))
        if l == 0:
            return 0.0
        rmse = np.sqrt(np.mean((o1[:l] - o2[:l]) ** 2))
        return max(0.0, 100.0 / (1 + rmse))

    def tempo_threshold_score(t1, t2):
        return max(0.0, 100.0 - abs(t1 - t2))

    scores = {
        "pitch_dtw": round(dtw_pitch_score(pitch_user, pitch_ref), 2),
        "transpose": round(transpose_score(pitch_user, pitch_ref), 2),
        "pitch_chunk": round(pitch_chunk_score(pitch_user, pitch_ref), 2),
        "mfcc_cos": round(mfcc_cosine_score(mfcc_stack_user, mfcc_stack_ref), 2),
        "mfcc_chunk": round(mfcc_chunk_rmse(mfcc_stack_user, mfcc_stack_ref), 2),
        "onset_match": round(onset_match_score(onset_pos_user, onset_pos_ref), 2),
        "onset_chunk": round(onset_chunk_rmse(onset_user_norm, onset_ref_norm), 2),
        "tempo_thresh": round(tempo_threshold_score(tempo_user, tempo_ref), 2)
    }
    return scores

def compute_grouped_scores(scores: dict, weights: dict, categories: dict) -> dict:
    group_totals = defaultdict(float)
    group_weights = defaultdict(float)
    for k, v in scores.items():
        group = categories.get(k, "unknown")
        w = weights.get(k, 0)
        group_totals[group] += v * w
        group_weights[group] += w
    return {
        group: round(group_totals[group] / group_weights[group], 2)
        for group in group_totals
        if group_weights[group]
    }

def compute_weighted_score(scores: dict, weights: dict) -> float:
    total_weight = sum(weights.get(k, 0) for k in scores)
    if total_weight == 0:
        return 0.0
    weighted_sum = sum(scores[k] * weights.get(k, 0) for k in scores)
    return round(weighted_sum / total_weight, 2)

all_weights = {
    "pitch_dtw": 0.12, 
    "transpose": 0.12, 
    "pitch_chunk": 0.06,
    "mfcc_cos": 0.16,
    "mfcc_chunk": 0.17,
    "onset_match": 0.11,
    "onset_chunk": 0.11,
    "tempo_thresh": 0.15
}

categories = {
    "pitch_dtw": "tune", 
    "transpose": "tune", 
    "pitch_chunk": "tune",
    "mfcc_cos": "tone",
    "mfcc_chunk": "tone",
    "onset_match": "beat",
    "onset_chunk": "beat",
    "tempo_thresh": "beat"
}

@celery.task(name="ai.app.tasks.audio_analysis.run_audio_analysis")
def run_audio_analysis(user_audio_url: str, original_json_url: str):
    tmp_audio_path = None
    try:
        try:
            audio_response = requests.get(user_audio_url)
            audio_response.raise_for_status()
        except RequestException as e:
            raise RuntimeError(f"사용자 오디오 다운로드 실패: {e}")
        with tempfile.NamedTemporaryFile(delete=False, suffix=".wav") as tmp_audio_file:
            tmp_audio_file.write(audio_response.content)
            tmp_audio_path = tmp_audio_file.name

        user_y, user_sr = librosa.load(tmp_audio_path, sr=None)
        user_y = user_y.astype(np.float32)

        # 원본 특징 JSON 다운로드
        try:
            json_response = requests.get(original_json_url)
            json_response.raise_for_status()
            features_json = json_response.json()
        except RequestException as e:
            raise RuntimeError(f"features.json 다운로드 실패: {e}")

        # 원본 특징 값 로딩
        pitch_org = np.array(features_json["pyin"])
        mfcc_org = np.array(features_json["mfcc"])
        onset_env_org = np.array(features_json["onset"])
        tempo_org = float(features_json["beat"])

        # 사용자 특징 추출
        pitch_user = extract_pyin_pitch(user_y, user_sr)
        mfcc_user = extract_mfcc_stack(user_y, user_sr)
        onset_env_user = extract_onset_strength(user_y, user_sr)
        onset_pos_user = safe_onset_detect(user_y, user_sr)
        tempo_user = extract_tempo(user_y, user_sr, onset_env_user)
        rms_user = librosa.feature.rms(y=user_y)[0]
        rms_org = np.ones_like(rms_user)  # 원본 오디오가 없으므로 길이 맞춤용 (선택)

        # 점수 평가
        scores = evaluate_user_scores(
            pitch_user, pitch_org,
            onset_env_user, onset_env_org,
            onset_pos_user, onset_pos_user * 0,  # onset_pos_org 없음 처리
            tempo_user, tempo_org,
            mfcc_user, mfcc_org,
            rms_user, rms_org,
            user_sr
        )

        grouped = compute_grouped_scores(scores, all_weights, categories)
        total = compute_weighted_score(scores, all_weights)

        final_result = {
            "tune_score": grouped.get("tune", 0.0),
            "tone_score": grouped.get("tone", 0.0),
            "beat_score": grouped.get("beat", 0.0)
        }

        return final_result

    except Exception:
        return {
            "tune_score": 0.0,
            "beat_score": 0.0,
            "tone_score": 0.0
        }

    finally:
        if tmp_audio_path and os.path.exists(tmp_audio_path):
            os.remove(tmp_audio_path)