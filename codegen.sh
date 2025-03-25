#!/bin/bash
set -e

# 🛑 Docker Compose 종료
echo "\n🚨 Stopping... 존재하는 도커 컨테이너를 정리합니다."
docker-compose down

echo "\n🧹 Cleaning... 파일을 정리합니다.."
# 🧹 파일 삭제
./cleanup.sh

echo "\n🚀 Building and starting... 코드 생성 후 목 서버를 실행합니다."
# 🚀 코드 생성 및 목 서버 실행
docker-compose up --build