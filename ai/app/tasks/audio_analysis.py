import os
from dotenv import load_dotenv
import socket

from pathlib import Path
from celery import Celery, shared_task
from ai.celery_app import celery

from minio import Minio
from io import BytesIO
from .redis_cache import get_cache_feature, set_cache_feature
from minio.error import S3Error

import requests
from requests.exceptions import RequestException
import tempfile
import traceback
import re

import json
import numpy as np
import librosa
from fastdtw import fastdtw
import scipy
from scipy.spatial.distance import euclidean, cdist
import soundfile as sf
scipy.signal.hann = scipy.signal.windows.hann

# 걸리는 시간 테스트(배포시에는 지울 예정)
import time

# 1. docker-compose에서 받은 환경 변수 먼저
env_mode = os.getenv("ENV_MODE")

# 2. 없으면 .env 파일에서
if not env_mode:
    from dotenv import load_dotenv
    load_dotenv()
    env_mode = os.getenv("ENV_MODE", "development")

# 3. 정제
env_mode = env_mode.strip().lower().replace(".local", "").strip(".")


# ✅ MinIO 환경 변수 불러오기
MINIO_HOST = os.getenv("MINIO_HOST", "localhost")
MINIO_PORT = os.getenv("MINIO_PORT", "9001")
MINIO_ENDPOINT = f"{MINIO_HOST}:{MINIO_PORT}"
MINIO_USERNAME = os.getenv("MINIO_USERNAME")
MINIO_PASSWORD = os.getenv("MINIO_PASSWORD")
MINIO_BUCKET_NAME = os.getenv("_NAME", "original-mp3-audio-bucket")

# ✅ MinIO 클라이언트 생성
minio_client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_USERNAME,
    secret_key=MINIO_PASSWORD,
    secure=False  # 개발 환경에서는 보통 False
)

# ✅ Celery 인스턴스 생성
celery = Celery(
    "tasks",
    broker=os.getenv("CELERY_BROKER_URL", "redis://localhost:6379/0"),
    backend=os.getenv("CELERY_RESULT_BACKEND", "redis://localhost:6379/0")
)

# 동시 작업자 수를 설정 (병렬 처리 수준 결정)
celery.conf.update(
    worker_concurrency=4,  # 동시에 실행할 작업자 수
    task_acks_late=True,   # 작업이 완료된 후에만 메시지 확인
    task_time_limit=120,    # 작업의 최대 실행 시간 (초)
    worker_prefetch_multiplier=4  # 각 작업자가 한 번에 가져올 메시지 수
)


# minio 연결 테스트
def check_minio_connection():
    print(f"🔗 MinIO 연결 테스트... {MINIO_ENDPOINT} → {MINIO_BUCKET_NAME}")
    print(f"🔗 MinIO 연결 테스트... {minio_client.bucket_exists(MINIO_BUCKET_NAME)}")
    try:
        found = minio_client.bucket_exists(MINIO_BUCKET_NAME)
        if not found:
            print(f"❌ 버킷 '{MINIO_BUCKET_NAME}' 존재하지 않음")
            return False
        print(f"✅ MinIO 연결 성공! 버킷 '{MINIO_BUCKET_NAME}' 접근 가능")
        return True
    except S3Error as e:
        print(f"❌ MinIO 연결 실패: {e.code} - {e.message}")
        return False
    except Exception as e:
        print(f"❌ MinIO 예외 발생: {e}")
        return False

# ✅ 사용자 오디오 로컬에서 불러오기 (tests/cover_audio/{곡제목}(커버).wav)
def load_user_audio_from_tests(song_id, instrument_id):
    file_path = f"../../tests/cover_audio/{song_id}/{instrument_id}/CHALLENGE.mp3"
    print(file_path)
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"사용자 오디오가 존재하지 않습니다: {file_path}")
    
    print(f"🔍 사용자 오디오 로드: {file_path}")
    y, sr = librosa.load(file_path, sr=None)
    y = y.astype(np.float32)
    print(f"🎧 사용자 오디오 길이: {len(y)}, 샘플링 레이트: {sr}")
    return y, sr

# ✅ MinIO에서 원곡 특징 불러오기 (곡제목/features.json)
def load_json_from_minio(song_id, file_name):
    print(f"🔍 MinIO에서 JSON 파일 로드 시작: {song_id}/{file_name}")
    object_path = f"DB/{song_id}/{file_name}"
    try:
        response = minio_client.get_object(MINIO_BUCKET_NAME, object_path)
        print(f"📥 MinIO에서 JSON 파일 로드: {response}")
        return json.load(BytesIO(response.read()))
    except Exception as e:
        raise RuntimeError(f"MinIO에서 JSON 로드 실패: {object_path} → {e}") 

# ✅ MinIO에서 원곡 waveform 불러오기 (곡제목/악기명/악기명.npz)
def load_npz_from_minio(song_id, instrument_id):
    print(f"🔍 MinIO에서 npz 파일 로드 시작: {song_id}/{instrument_id}")
    object_path = f"DB/{song_id}/{instrument_id}/{instrument_id}.npz"
    try:
        response = minio_client.get_object(MINIO_BUCKET_NAME, object_path)
        print(f"📥 MinIO에서 npz 파일 로드: {response}")
        buffer = BytesIO(response.read())
        data = np.load(buffer)

        # 디버깅 출력
        print(f"📦 npz 파일 키: {list(data.keys())}")
        if "sr" not in data or data["sr"] is None:
            raise ValueError(f"❌ npz 파일에 'sr' 값이 없습니다: {object_path}")
        if "y" not in data:
            raise ValueError(f"❌ npz 파일에 'y' 값이 없습니다: {object_path}")

        return data["y"], int(data["sr"])
    except Exception as e:
        raise RuntimeError(f"MinIO에서 npz 로드 실패: {object_path} → {e}")

# ✅ 분석 함수 (유사도 계산)
def compare_features(user_y, user_sr, original_features):
    print("🔬 분석 시작...")
    
    result = {}
    
    # Debug info
    print(f"🎧 user_y length: {len(user_y)}, sr: {user_sr}")
    print(f"📈 pyin 원본 길이: {len(original_features['pyin'])}")
    print(f"📈 onset 원본 길이: {len(original_features['onset'])}")
    print(f"📈 mfcc 원본 shape: {np.array(original_features['mfcc']).shape}")

    # 🎵 Pitch (pyin)
    t0 = time.time()
    target_sr = 22050
    y_resampled = librosa.resample(user_y, orig_sr=user_sr, target_sr=target_sr)
    pitch_user = np.nan_to_num(librosa.pyin(y_resampled, fmin=librosa.note_to_hz('C2'),
                                            fmax=librosa.note_to_hz('C7'))[0])
    print(f"📈 pitch_user length: {len(pitch_user)} / {time.time() - t0:.2f}초")
    
    t0 = time.time()
    pitch_ref = original_features['pyin'][::2]  # 2배 더 촘촘하게
    print(f"📈 pitch_ref length: {len(pitch_ref)} / {time.time() - t0:.2f}초")
    
    t0 = time.time()
    dist, _ = fastdtw(pitch_user.reshape(-1, 1),
                      np.array(pitch_ref).reshape(-1, 1),
                      dist=euclidean)
    result["tune_score"] = round(max(0, 100 - (dist / len(pitch_ref)) * 0.5), 2)
    print(f"📈 tune_score: {result['tune_score']} / {time.time() - t0:.2f}초")

    # 🥁 beat
    t0 = time.time()
    tempo_user = librosa.beat.beat_track(y=user_y, sr=user_sr)[0]
    print(f"📈 tempo_user: {tempo_user} / {time.time() - t0:.2f}초")
    result["beat_score"] = round(max(0 , 100 - abs(original_features['beat'] - tempo_user)), 2)

    # 🪘 Rhythm (onset strength)
    t0 = time.time()
    onset_user = librosa.onset.onset_strength(y=user_y, sr=user_sr)
    print(f"📈 onset_user length: {len(onset_user)} / {time.time() - t0:.2f}초")
    t0 = time.time()
    onset_ref = original_features['onset']
    print(f"📈 onset_ref length: {len(onset_ref)} / {time.time() - t0:.2f}초")
    dist, _ = fastdtw(onset_user.reshape(-1, 1),
                      np.array(onset_ref).reshape(-1, 1),
                      dist=euclidean)
    result["rhythm_score"] = round(max(0, 100 - (dist / len(onset_ref))), 2)

    # 🎼 Timbre (MFCC)
    t0 = time.time()
    mfcc_user = librosa.feature.mfcc(y=user_y, sr=user_sr, n_mfcc=13)
    avg_user = np.mean(mfcc_user, axis=1)
    print(f"📈 avg_user: 완료 / {time.time() - t0:.2f}초")
    t0 = time.time()
    avg_ref = [np.mean(mfcc_band) for mfcc_band in original_features['mfcc']]
    print(f"📈 avg_ref length: {len(avg_ref)} / {time.time() - t0:.2f}초")
    result["tone_score"] = round(max(0, 100 - euclidean(avg_user, avg_ref)), 2)

    # 📊 Total
    result["total_score"] = round(
        (result["tune_score"] + result["beat_score"] +
         result["rhythm_score"] + result["tone_score"]) / 4, 2
    )

    return result

# ✅ 결과 저장
RESULT_SAVE_PATH = "../../tests/analysis_result.json"

def save_analysis_result(result, save_path=RESULT_SAVE_PATH):
    formatted = {
        # "song_title": song_id,
        # "instrument_name": instrument_id,
        "tune_score": result["tune_score"],
        "beat_score": result["beat_score"] / 2,
        "tone_score": result["tone_score"],
    }
    with open(save_path, 'w') as f:
        json.dump(formatted, f, indent=2, ensure_ascii=False)
    print(f"📁 분석 결과 저장 완료: {save_path}")

"""
# 멀티 프로세스 간 캐시 공유 필요할 때 설정
from multiprocessing import Manager
from threading import Lock

# 프로세스 간 공유 가능한 캐시 딕셔너리
manager = Manager()
_feature_cache = manager.dict()
_feature_cache_lock = Lock() # 충돌 방지를 위한 락
"""


# 특징 사전 캐시
def get_cached_features(song_id, instrument_id):
    cache_key = f"{song_id}:{instrument_id}"
    print(f"🚀 [get_cached_features] 호출됨 - key: {cache_key}")

    cached = get_cache_feature(cache_key)
    if cached:
        print(f"✅ 캐시된 값 반환: {cache_key}")
        print(f"✅ 캐시된 값 타입: {type(cached)} | 키 목록: {list(cached.keys())}")
        return cached

    
    print(f"📥 캐시 MISS - MinIO에서 로드: {cache_key}")
    features_json = load_json_from_minio(song_id, "features.json")
    print(f"📥 MinIO JSON 타입: {type(features_json)} | 키: {features_json.keys()}")
    
    if "instruments" not in features_json:
        raise KeyError(f"❌ features.json에 'instruments' 키 없음")

    if instrument_id not in features_json["instruments"]:
        raise KeyError(f"❌ features.json 내에 {instrument_id} 항목이 없습니다.")
     
    instrument_features = features_json["instruments"][instrument_id]
    print(f"🎯 가져온 특징 타입: {type(instrument_features)} | 키 목록: {list(instrument_features.keys())}")

    set_cache_feature(cache_key, instrument_features)

    return instrument_features

# ✅ Celery 태스크로 래핑된 분석 함수
@celery.task(name="ai.app.tasks.audio_analysis.run_audio_analysis")
def run_audio_analysis(user_audio_url: str, original_json_url: str):
    tmp_audio_path = None  # finally에서 접근할 수 있도록 미리 선언
    try:
        print(f"🎧 Celery 분석 요청 (URL 기반)")
        print(f"🔗 user_audio_url: {user_audio_url}")
        print(f"🔗 original_json_url: {original_json_url}")
        
        # 1. 사용자 오디오 다운로드
        try:
            audio_response = requests.get(user_audio_url)
            print(f"✅ 오디오 응답 코드: {audio_response.status_code}")
            audio_response.raise_for_status()
        except RequestException as e:
            raise RuntimeError(f"❌ 사용자 오디오 다운로드 실패: {e}")

        # # ✅ 파일명 추출 from Content-Disposition
        # cd_header = audio_response.headers.get("Content-Disposition", "")
        # filename_match = re.search(r'filename="(.+?)"', cd_header)

        # if not filename_match:
        #     raise ValueError("❌ Content-Disposition 헤더에서 파일명을 찾을 수 없습니다.")

        # filename = filename_match.group(1)
        # print(f"📄 추출된 파일명: {filename}")

        # # ✅ 파일명에서 instrument_id, song_id 추출
        # parts = filename.split("_")
        # if len(parts) < 3:
        #     raise ValueError("❌ 파일명 형식이 잘못되었습니다. (tier_instrument_artist-title.wav)")

        # instrument_id = parts[1]
        # song_id = os.path.splitext("_".join(parts[2:]))[0]  # "가수-곡"

        # print(f"📄 다운로드 파일명: {filename}")
        # print(f"🎼 추출된 song_id: {song_id}")
        # print(f"🥁 추출된 instrument_id: {instrument_id}")

        # 🔊 임시 디렉토리에서 오디오 저장
        with tempfile.NamedTemporaryFile(delete=False, suffix=".wav") as tmp_audio_file:
            tmp_audio_file.write(audio_response.content)
            tmp_audio_path = tmp_audio_file.name
            print(f"✅ 오디오 임시 저장: {tmp_audio_path}")

        # 캐시 코드(일단 홀드)
        # original_features = get_cached_features(song_id, instrument_id)
        
        # 🎧 오디오 로드
        user_y, user_sr = librosa.load(tmp_audio_path, sr=None)
        user_y = user_y.astype(np.float32)

        # 📥 원곡 JSON 다운로드
        try:
            json_response = requests.get(original_json_url)
            json_response.raise_for_status()
            features_json = json_response.json()
            print("원곡 json 다운로드 성공")
            print(features_json['pyin'][0])
        except RequestException as e:
            raise RuntimeError(f"❌ 원곡 features.json 다운로드 실패: {e}")

        # ✅ 유사도 분석
        result = compare_features(user_y, user_sr, features_json)


        # ✅ 결과 추출 (요구된 형식으로 정리)
        final_result = {
            "tune_score": result["tune_score"],
            "beat_score": round((result["beat_score"] + result["rhythm_score"]) / 2, 2),
            "tone_score": result["tone_score"]
        }

        print(f"📊 분석 완료: {final_result}")
        
        # 🗂 결과 저장 (선택)
        # save_analysis_result(final_result)

        return final_result
    
    except Exception as e:
        print(f"❌ 분석 실패: {e}")
        traceback.print_exc()
        return {
            "tune_score": 0.0,
            "beat_score": 0.0,
            "tone_score": 0.0
        }

    finally:
        # 🧹 임시파일 삭제
        if tmp_audio_path:
            try:
                os.remove(tmp_audio_path)
                print(f"🧹 임시 오디오 파일 삭제 완료: {tmp_audio_path}")
            except Exception as e:
                print(f"⚠️ 임시파일 삭제 실패: {e}")

        ### 아래는 로컬 기반 작동 코드
        # # 사용자 로드
        # user_y, user_sr = load_user_audio_from_tests(song_id, instrument_id)

        # print(f"🎧 사용자 로드 완료")

        # print(f"캐시 데이터 불러오기 ")
        # # MinIO에서 특징 / waveform 불러오기
        # original_features = get_cached_features(song_id, instrument_id)
        # print(f'original_features dtype: {type(original_features)}')

        # # waveform 로드로 필요시 사용
        # # y_ref, sr_ref = load_npz_from_minio(song_id, instrument_id)


        # # 분석 수행
        # result = compare_features(user_y, user_sr, original_features)

        # # Postman 테스트용 반환 구조
        # save_analysis_result(song_id, instrument_id, result)
        # return result
    

# ✅ 테스트 실행 함수 (직접 실행용)
if __name__ == "__main__":
    # 예시 곡/파트 (수동 실행 확인용)
    user_audio_url = "QWER - 불꽃놀이"
    original_json_url = "drums"

    response = requests.get(user_audio_url)
    response.raise_for_status()
    
    # 파일명 추출
    disposition = response.headers.get("Content-Disposition", "")
    filename = "unknown.wav"
    if "filename=" in disposition:
        filename = disposition.split("filename=")[-1].strip().strip("\"")
    print(f"📄 다운로드 파일명: {filename}")

    # 🧠 파일 이름에서 song_id, instrument_id 추출
    _, instrument_id, song_info_with_ext = filename.split("_", 2)
    song_id = os.path.splitext(song_info_with_ext)[0]  # 확장자 제거
    print(f"🎼 추출된 song_id: {song_id}")
    print(f"🥁 추출된 instrument_id: {instrument_id}")

    print("🧪 테스트 실행 중...")
        # 연결 먼저 확인
    if not check_minio_connection():
        print("⛔ MinIO 연결 실패. 분석 중단.")
    else:
        print(f"🎵 분석 곡: {song_id}, 파트: {instrument_id}")
        result = run_audio_analysis(song_id, instrument_id)
        print(json.dumps(result, indent=2, ensure_ascii=False))
