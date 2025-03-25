#!/bin/bash
set -e

# 파일 삭제 명령어
./cleanup.sh

docker-compose up --build