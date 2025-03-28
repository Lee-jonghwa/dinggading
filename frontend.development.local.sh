#!/bin/bash
set -e

echo -e "DDDDDDDDDDDDD                     GGGGGGGGGGGGG     DDDDDDDDDDDDD"
echo -e "D::::::::::::DDD               GGG::::::::::::G     D::::::::::::DDD"
echo -e "D:::::::::::::::DD           GG:::::::::::::::G     D:::::::::::::::DD"
echo -e "DDD:::::DDDDD:::::D         G:::::GGGGGGGG::::G     DDD:::::DDDDD:::::D"
echo -e "  D:::::D    D:::::D       G:::::G       GGGGGG       D:::::D    D:::::D"
echo -e "  D:::::D     D:::::D     G:::::G                     D:::::D     D:::::D"
echo -e "  D:::::D     D:::::D     G:::::G                     D:::::D     D:::::D"
echo -e "  D:::::D     D:::::D     G:::::G    GGGGGGGGGG       D:::::D     D:::::D"
echo -e "  D:::::D     D:::::D     G:::::G    G::::::::G       D:::::D     D:::::D"
echo -e "  D:::::D     D:::::D     G:::::G    GGGGG::::G       D:::::D     D:::::D"
echo -e "  D:::::D     D:::::D     G:::::G        G::::G       D:::::D     D:::::D"
echo -e "  D:::::D    D:::::D       G:::::G       G::::G       D:::::D    D:::::D"
echo -e "DDD:::::DDDDD:::::D         G:::::GGGGGGGG::::G     DDD:::::DDDDD:::::D"
echo -e "D:::::::::::::::DD           GG:::::::::::::::G     D:::::::::::::::DD"
echo -e "D::::::::::::DDD               GGG::::::GGG:::G     D::::::::::::DDD"
echo -e "DDDDDDDDDDDDD                     GGGGGG   GGGG     DDDDDDDDDDDDD"

echo -e "프론트 빌드 후 실행 스크립트"

# 파일 삭제 명령어
echo -e "\e[1;34m🔧 Shut Down: 현재 실행 중인 환경을 닫는 중 입니다.. 🔧\e[0m"  # 파란색, 볼드 텍스트
docker-compose -f docker-compose.dev.local.yaml down


# 포트 8080을 사용하는 프로세스의 PID 찾기
PID=$(netstat -ano | grep ":8080" | grep "LISTENING" | awk '{print $5}')

if [ -z "$PID" ]; then
  echo "포트 8080을 사용하는 프로세스가 없습니다."
else
  echo "포트 8080을 사용하는 프로세스 PID: $PID"
  echo "taskkill //F //PID $PID 를 입력해서 프로세스를 죽이세요"
  exit 1
fi

echo -e "\e[1;34m🔧 Codegen을 실행합니다. 🔧\e[0m"  # 파란색, 볼드 텍스트
./codegen.sh

echo -e "\e[1;32m✅ Building: 프론트 엔드 파일을 빌드하는 중 입니다...✅\e[0m"  # 초록색, 볼드 텍스트
docker-compose -f docker-compose.dev.local.yaml build next

# 클린업 완료 및 빌드 시작
echo -e "\e[1;32m✅ Building: 개발 환경을 구성하는 중 입니다...✅\e[0m"  # 초록색, 볼드 텍스트
docker-compose -f docker-compose.dev.local.yaml up --no-build