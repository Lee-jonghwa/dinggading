import shutil
import os
import json
from pathlib import Path
from dotenv import load_dotenv

from fastapi import FastAPI, UploadFile, Form
from pydantic import BaseModel

import asyncio
from aio_pika import connect_robust, Message, ExchangeType

from ai.app.tasks.audio_analysis import run_audio_analysis
from ai.app.routes import download_proxy
from ai.app.routes import presigned_url
from celery.result import AsyncResult

# 현재 파일(ai/celery_worker.py) 기준으로 루트 경로 계산
current_dir = Path(__file__).resolve()
project_root = current_dir.parent.parent.parent  # /app/ai/app → /app

# 환경 변수 파일 경로 설정
env_mode = os.getenv("ENV_MODE", "development")
env_path = project_root / "infra" / "env" / f".env.{env_mode}"

load_dotenv(dotenv_path=env_path)


# 🌟 FastAPI 인스턴스 생성
app = FastAPI()

# 라우터 등록
app.include_router(download_proxy.router)
app.include_router(presigned_url.router)

# 🔐 RabbitMQ 연결 정보 (환경변수 기반)
RABBITMQ_URL = (
    f"amqp://{os.getenv('RABBITMQ_DEFAULT_USER')}:{os.getenv('RABBITMQ_DEFAULT_PASS')}"
    f"@{os.getenv('RABBIT_MQ_HOST')}:{os.getenv('RABBIT_MQ_PORT')}/"
)

# 🧭 익스체인지/큐/라우팅 키 정의
REQUEST_EXCHANGE_NAME = "audio-analysis-request-exchange"
RESPONSE_EXCHANGE_NAME = "audio-analysis-response-exchange"
REQUEST_QUEUE_NAME = "audio-analysis-request"
RESPONSE_ROUTING_KEY = "audio.analysis.response"

# ✅ 메시지 포맷 정의 (Spring → FastAPI)
class AnalyzeRequest(BaseModel):
    recordFilename: str
    originalFilename: str
    attemptId : int

# ✅ 메시지 포맷 정의 (FastAPI → Spring)
class AnalyzeResponse(BaseModel):
    attemptId : int
    beatScore: float
    tuneScore: float
    toneScore: float

# 🚀 FastAPI 시작 시 RabbitMQ Consumer 실행
@app.on_event("startup")
async def startup_event():
    print("✅ startup_event 실행됨")
    loop = asyncio.get_event_loop()
    loop.create_task(start_rabbitmq_consumer())


# 📥 RabbitMQ 요청 메시지 수신 함수
async def start_rabbitmq_consumer():
    # 🧷 연결 설정
    connection = await connect_robust(RABBITMQ_URL)
    channel = await connection.channel()
    await channel.set_qos(prefetch_count=4)   # 동시에 4개 처리

    # 📩 요청용 익스체인지/큐 바인딩
    request_exchange = await channel.declare_exchange(
        REQUEST_EXCHANGE_NAME,
        ExchangeType.DIRECT,
        durable=True
        )
    queue = await channel.declare_queue(
        REQUEST_QUEUE_NAME,
        durable=True,
        arguments={
            "x-message-ttl": 60000,     # 60초
            "x-max-length": 1000        # 최대 1000개 메시지
        }
    )    
    print("✅ 큐 바인딩 완료됨")
    await queue.bind(request_exchange, routing_key="audio.analysis.request")

    # ✅ 응답 익스체인지도 미리 선언해놓자
    response_exchange = await channel.declare_exchange(
        RESPONSE_EXCHANGE_NAME,
        ExchangeType.DIRECT,
        durable=True
    )

    print("✅ 큐 구독 시작됨")  # queue.iterator() 바로 전에
    
    # 🔄 메시지 추적을 위한 사전
    active_tasks = {}
    
    # 🔁 메시지 반복 수신
    async with queue.iterator() as queue_iter:
        async for message in queue_iter:
            try:
                body = message.body.decode()
                data = json.loads(body)
                print("🔔 요청 수신:", data)
                
                request = AnalyzeRequest(**data)
                
                # Celery 분석 태스크 실행 (비동기적으로)
                task = run_audio_analysis.delay(
                    request.recordFilename,
                    request.originalFilename
                )
                
                # 메시지 ID와 함께 태스크 추적
                active_tasks[task.id] = {
                    "task": task,
                    "request": request,
                    "message": message
                }
                
                # 메시지는 아직 확인하지 않음 (작업 완료 후 확인할 예정)
                
            except Exception as e:
                print("❌ 요청 처리 실패:", e)
                # 오류 발생 시에만 메시지 확인
                await message.ack()
                
    # 백그라운드 태스크로 결과 확인
    asyncio.create_task(check_task_results(channel, response_exchange, active_tasks))

async def check_task_results(channel, response_exchange, active_tasks):
    """백그라운드에서 완료된 태스크 확인 및 처리"""
    while True:
        # 복사본으로 루프 (순회 중 수정 방지)
        task_ids = list(active_tasks.keys())
        
        for task_id in task_ids:
            task_info = active_tasks[task_id]
            task = task_info["task"]
            
            # 완료된 태스크만 처리
            if task.ready():
                try:
                    # 태스크 결과 가져오기 (블로킹 없음)
                    result = task.result
                    
                    if not isinstance(result, dict):
                        result = {"tuneScore": 0.0, "toneScore": 0.0, "beatScore": 0.0}
                    
                    result["attemptId"] = task_info["request"].attemptId
                    
                    # 응답 발행
                    await response_exchange.publish(
                        Message(body=json.dumps(result).encode()),
                        routing_key=RESPONSE_ROUTING_KEY
                    )
                    
                    print("📤 응답 발행:", result)
                    
                except Exception as e:
                    print(f"❌ 결과 처리 실패: {e}")
                
                finally:
                    # 메시지 확인 (작업 완료됨)
                    await task_info["message"].ack()
                    # 추적 사전에서 제거
                    del active_tasks[task_id]
        
        # 잠시 대기 후 다시 확인
        await asyncio.sleep(0.1)

@app.get("/health/")
def health_check():
    print(run_audio_analysis)
    return {"status": "OK"}
