# Dockerfile for AI service

# Base stage with common dependencies
FROM python:3.11-bullseye as base

# 시스템 라이브러리 설치 (변경이 적은 레이어)
RUN apt-get update && \
    apt-get install -y \
    ffmpeg \
    libsndfile1 \
    build-essential \
    libblas-dev \
    liblapack-dev \
    gfortran \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 작업 디렉토리 설정
WORKDIR /app

# Python 의존성 레이어 (캐싱 향상을 위해 분리)
FROM base as dependencies
COPY ./ai/requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Development stage
FROM dependencies as development
# 소스코드 복사
COPY ./ai /app/ai

# 환경 파일 복사 (필요한 경우 주석 해제)
COPY ./infra/env /app/infra/env

# 기본 실행 명령 (FastAPI)
CMD ["uvicorn", "ai.app.main:app", "--host", "0.0.0.0", "--port", "8000"]

# Production stage (최종 런타임 이미지)
FROM dependencies as production
# 소스 코드만 복사 (테스트 코드 제외)
COPY ./ai /app/ai
# 환경 파일 복사 (필요한 경우)
# COPY ./infra/env /app/infra/env

# 프로덕션 환경 변수 설정 (선택 사항)
ENV ENVIRONMENT=production

# FastAPI 실행 (경로 수정: 개발 환경과 일관성 유지)
CMD ["uvicorn", "ai.app.main:app", "--host", "0.0.0.0", "--port", "8000"]