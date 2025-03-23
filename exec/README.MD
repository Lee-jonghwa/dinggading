
# 🌟 DING-GA-DING 프로젝트 포팅 매뉴얼 🌟

## 📑 목차
- [시스템 구성 요소 및 버전](#-시스템-구성-요소-및-버전)
- [환경 변수 설정](#️-환경-변수-설정)
- [배포 방법](#-배포-방법)
- [특이 사항 및 주의점](#-특이-사항-및-주의점)
- [데이터베이스 정보](#-데이터베이스-정보)
- [프로젝트 구조](#-프로젝트-구조)
- [트러블슈팅](#️-트러블슈팅)
- [도움이 필요하신가요?](#-도움이-필요하신가요)

## 🚀 시스템 구성 요소 및 버전
[목차로 돌아가기](#-목차)

### 🔹 백엔드
- **JVM**: ☕ OpenJDK 17 (Alpine 기반)
- **프레임워크**: 🍃 Spring Boot 3.4.3
- **빌드 도구**: 🐘 Gradle 8.2
- **데이터베이스**: 🐬 MySQL 8.0

### 🔹 프론트엔드
- **런타임**: 📦 Node.js 18 (Alpine 기반)
- **프레임워크**: ⚛️ Next.js

### 🔹 웹 서버
- **서버**: 🌐 Nginx (Alpine 버전)

### 🔹 Docker 관련
- 🐳 모든 서비스는 Docker 컨테이너로 실행
- 🐙 Docker Compose를 사용하여 서비스 조율

### 🔹 API 생성 도구
- 📝 OpenAPI Generator CLI
- 🔄 OpenAPI Merge CLI

### 🔹 CI/CD
- 📦 Gitlab Runner

## ⚙️ 환경 변수 설정
[목차로 돌아가기](#-목차)

프로젝트에서는 다음 환경 파일들을 사용합니다:

- 🟢 `./infra/env/.env.development.local`: 로컬 개발 환경용 환경 변수
- 🟠 `./infra/env/.env.production.local`: 로컬 프로덕션 테스트용 환경 변수
- 🔴 `./infra/env/.env.production`: 실제 프로덕션 배포용 환경 변수

### 📋 필수 환경 변수 (MySQL 설정)
```
MYSQL_DATABASE=your_database_name
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_USER=your_user
MYSQL_PASSWORD=your_password
```

### 📱 프론트엔드 환경 변수
빌드 시에 다음 ARG 값들이 설정됩니다:

- `NEXT_PUBLIC_API_BASE_URL`: 백엔드 API 접근 URL (기본값: spring:8080)
- `NEXT_PUBLIC_ENV_MODE`: 환경 모드 (DEVELOP_LOCAL, PRODUCTION_LOCAL, DOCKER_FILE)

## 🚀 배포 방법
[목차로 돌아가기](#-목차)

### 💻 로컬 개발 환경
개발 모드에서 프로젝트를 실행하려면:

```bash
# 이전 실행 중인 컨테이너 정리 및 자동 생성된 코드 정리 후 개발 환경 실행
./development.local.sh

# 백그라운드에서 실행하려면
./development.local.sh -d
```

### 🧪 로컬 프로덕션 환경
로컬에서 프로덕션 모드로 테스트하려면:

```bash

# 프로덕션 모드로 로컬 실행
./production.local.sh
```
```bash

# 백그라운드에서 실행하려면
./production.local.sh -d
```

### 🌍 실제 프로덕션 배포
실제 프로덕션 환경에 배포하려면 다음 단계를 따릅니다:

1. Docker 네트워크 생성 (최초 한 번만):
```bash

docker network create ding-ga-ding-network
```

2. 프로덕션 배포 스크립트 실행:
```bash

docker-compose -f docker-compose.prod.yaml up -d
```

## 🔍 특이 사항 및 주의점
[목차로 돌아가기](#-목차)

### 📑 OpenAPI 코드 생성 프로세스
1. 🔄 `openapi-merger` 서비스가 API 스펙을 하나로 병합
2. 🔙 `spring-generator`가 백엔드 코드 생성
3. 🔝 `axios-generator`가 프론트엔드 API 클라이언트 코드 생성

### 🚪 포트 확인
- 웹 서버(Nginx): 🌐 80
- 백엔드(Spring): 🍃 8080
- 프론트엔드(Next.js): ⚛️ 3000

### 🏗️ 빌드 프로세스
프로젝트는 다음과 같은 멀티 스테이지 빌드 프로세스를 사용합니다:
- `base` → `development` → `test` → `build` → `production`

### 🚫 포트 충돌 해결
스크립트는 8080 포트 사용 여부를 확인하고, 사용 중이면 알림을 제공합니다:
```
taskkill //F //PID $PID
```
명령으로 해당 프로세스를 종료할 수 있습니다(Windows 환경).

## 🐬 데이터베이스 정보
[목차로 돌아가기](#-목차)

### 🔐 MySQL 접속 정보
- **호스트**: mysql
- **포트**: 3306 (기본값)
- **데이터베이스**: 환경 변수 `MYSQL_DATABASE`에 정의됨
- **사용자**: 환경 변수 `MYSQL_USER`에 정의됨
- **비밀번호**: 환경 변수 `MYSQL_PASSWORD`에 정의됨

### 📝 문자 인코딩 설정
- 문자셋: utf8mb4
- 콜레이션: utf8mb4_unicode_ci

## 📂 프로젝트 구조
[목차로 돌아가기](#-목차)

```
.
├── 📁 backend/                # 백엔드 Spring Boot 프로젝트
│   ├── 📁 generated/          # OpenAPI로 생성된 코드 (git에 포함 안 됨)
│   └── 📁 src/                # 백엔드 소스 코드
├── 📁 frontend/               # 프론트엔드 Next.js 프로젝트
│   └── 📁 generated/          # OpenAPI로 생성된 API 클라이언트 (git에 포함 안 됨)
├── 📁 docs/                   # OpenAPI 스펙 문서
├── 📁 infra/                  # 인프라 관련 파일
│   ├── 📁 dockerfiles/        # 서비스별 Dockerfile
│   └── 📁 env/                # 환경변수 파일
├── 📁 nginx/                  # Nginx 설정
│   └── 📁 conf.d/             # Nginx 설정 파일
├── 📁 generator/              # OpenAPI 코드 생성 템플릿
├── 🧹 cleanup.sh              # 생성된 코드 정리 스크립트
├── 💻 development.local.sh    # 로컬 개발 환경 실행 스크립트
├── 🏭 production.local.sh     # 로컬 프로덕션 환경 실행 스크립트
├── 🐳 docker-compose.yml      # 기본 도커 컴포즈 설정
├── 💻 docker-compose.dev.local.yaml   # 로컬 개발 환경용 도커 컴포즈
├── 🏭 docker-compose.prod.local.yaml  # 로컬 프로덕션 환경용 도커 컴포즈
└── 🌍 docker-compose.prod.yaml        # 실제 프로덕션 배포용 도커 컴포즈
```

## 🛠️ 트러블슈팅
[목차로 돌아가기](#-목차)

### 🔧 일반적인 문제 해결
1. **🚫 컨테이너가 시작되지 않는 경우**:
    - 로그 확인: `docker logs <container_id>`
    - 포트 충돌 확인: `netstat -ano | grep ":8080"`

2. **❌ API 코드 생성 실패**:
    - OpenAPI 스펙 문법 확인
    - 로그 확인: `docker logs <openapi-merger-container-id>`

3. **🔌 데이터베이스 연결 오류**:
    - 환경 변수 설정 확인
    - MySQL 컨테이너 상태 확인: `docker ps`

4. **⚠️ 생성된 코드가 올바르지 않은 경우**:
    - `./cleanup.sh` 실행 후 다시 시도

## 💌 도움이 필요하신가요?
[목차로 돌아가기](#-목차)

문제가 발생하거나 추가 도움이 필요하시면 언제든지 팀에 문의해주세요!
함께 해결해 나가겠습니다. 😊 항상 응원합니다! 🎉