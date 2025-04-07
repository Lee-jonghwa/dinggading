import os
import socket
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
from scipy.spatial.distance import euclidean
from requests.exceptions import RequestException
from minio.error import S3Error

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

minio_client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_USERNAME,
    secret_key=MINIO_PASSWORD,
    secure=False
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

def compare_features(user_y, user_sr, original_features):
    result = {}
    target_sr = 22050
    y_resampled = librosa.resample(user_y, orig_sr=user_sr, target_sr=target_sr)
    pitch_user = np.nan_to_num(librosa.pyin(y_resampled, fmin=librosa.note_to_hz('C2'),
                                            fmax=librosa.note_to_hz('C7'))[0])
    pitch_ref = original_features['pyin'][::2]
    dist, _ = fastdtw(pitch_user.reshape(-1, 1), np.array(pitch_ref).reshape(-1, 1), dist=euclidean)
    result["tune_score"] = round(max(0, 100 - (dist / len(pitch_ref)) * 0.5), 2)

    tempo_user = librosa.beat.beat_track(y=user_y, sr=user_sr)[0]
    result["beat_score"] = round(max(0, 100 - abs(original_features['beat'] - tempo_user)), 2)

    onset_user = librosa.onset.onset_strength(y=user_y, sr=user_sr)
    onset_ref = original_features['onset']
    dist, _ = fastdtw(onset_user.reshape(-1, 1), np.array(onset_ref).reshape(-1, 1), dist=euclidean)
    result["rhythm_score"] = round(max(0, 100 - (dist / len(onset_ref))), 2)

    mfcc_user = librosa.feature.mfcc(y=user_y, sr=user_sr, n_mfcc=13)
    avg_user = np.mean(mfcc_user, axis=1)
    avg_ref = [np.mean(mfcc_band) for mfcc_band in original_features['mfcc']]
    result["tone_score"] = round(max(0, 100 - euclidean(avg_user, avg_ref)), 2)

    result["total_score"] = round(
        (result["tune_score"] + result["beat_score"] +
         result["rhythm_score"] + result["tone_score"]) / 4, 2
    )

    return result

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

        try:
            json_response = requests.get(original_json_url)
            json_response.raise_for_status()
            features_json = json_response.json()
        except RequestException as e:
            raise RuntimeError(f"features.json 다운로드 실패: {e}")

        result = compare_features(user_y, user_sr, features_json)

        final_result = {
            "tune_score": result["tune_score"],
            "beat_score": round((result["beat_score"] + result["rhythm_score"]) / 2, 2),
            "tone_score": result["tone_score"]
        }

        return final_result

    except Exception:
        return {
            "tune_score": 0.0,
            "beat_score": 0.0,
            "tone_score": 0.0
        }

    finally:
        if tmp_audio_path:
            try:
                os.remove(tmp_audio_path)
            except:
                pass
