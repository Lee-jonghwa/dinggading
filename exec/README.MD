# 🌟 DING-GA-DING 프로젝트 포팅 매뉴얼 🌟

## 📑 목차
- [프로젝트 구조](#-프로젝트-구조)
- [시스템 구성 요소 및 버전](#-시스템-구성-요소-및-버전)
- [환경 변수 설정](#️-환경-변수-설정)
- [배포 방법](#-배포-방법)
- [특이 사항 및 주의점](#-특이-사항-및-주의점)
- [데이터베이스 정보](#-데이터베이스-정보)
- [트러블슈팅](#️-트러블슈팅)


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
├── 🧹 cleanup.sh              # 생성된 코드 정리 스크립트
├── 🏗️ codegen.sh             # API 코드 자동 생성 스크립트
├── 💻 development.sh          # 개발 환경 실행 스크립트
├── 🧪 development.mock.sh     # 목(Mock) 서버 개발 환경 실행 스크립트
├── 🐳 docker-compose.yml      # 기본 코드 생성 도커 컴포즈 설정
├── 💻 docker-compose.dev.yaml    # 개발 환경용 애플리케이션 서비스 도커 컴포즈
├── 🗃️ docker-compose.dev.db.yaml # 개발 환경용 데이터베이스 서비스 도커 컴포즈
├── 🧪 docker-compose.dev.mock.yaml # 목(Mock) 서버 개발 환경용 도커 컴포즈
└── 🌍 docker-compose.prod.yaml    # 프로덕션 배포용 도커 컴포즈
```

## 🚀 시스템 구성 요소 및 버전
[목차로 돌아가기](#-목차)

### 🔹 백엔드
- **JVM**: ☕ OpenJDK 17 (Alpine 기반)
- **프레임워크**: 🍃 Spring Boot 3.4.3
- **빌드 도구**: 🐘 Gradle 8.2

### 🔹 프론트엔드
- **런타임**: 📦 Node.js 18 (Alpine 기반)
- **프레임워크**: ⚛️ Next.js

### 🔹 데이터베이스 및 서비스
- **관계형 DB**: 🐬 MySQL 8.0
- **NoSQL DB**: 🍃 MongoDB 6.0
- **캐시 서버**: 🔄 Redis 7.0-alpine
- **메시지 큐**: 🐰 RabbitMQ 3.11-management
- **객체 저장소**: 📦 MinIO (latest)

### 🔹 Docker 관련
- 🐳 모든 서비스는 Docker 컨테이너로 실행
- 🐙 Docker Compose를 사용하여 서비스 조율

### 🔹 API 생성 도구
- 📝 OpenAPI Generator CLI
- 🔄 OpenAPI Merge CLI
- 🧪 Prism (API 목(Mock) 서버)

### 🔹 IDE 버전 정보
- 🧠 IntelliJ IDEA 2024.3.3 (Ultimate Edition)
- 🧮 Visual Studio Code 1.99.0

### 🔹 개발 환경
- 🖥️ 서버 OS: Ubuntu 22.04
- 🚀 CI/CD: GitLab Runner
- 💬 소통 도구: Mattermost, Notion, Miro

## ⚙️ 환경 변수 설정
[목차로 돌아가기](#-목차)

프로젝트에서는 다음 환경 파일들을 사용합니다:

- 🟢 `./infra/env/.env.development`: 개발 환경용 환경 변수
- 🔴 `./infra/env/.env.production`: 프로덕션 배포용 환경 변수

아래는 요청하신 환경변수 내용을 이모지와 마크다운 스타일을 반영하여 깔끔하게 정리한 `.md` 형식입니다. 각 영역은 보기 쉽게 구분되어 있으며, 실제 문서로 사용하기 적합하게 구성되어 있습니다.

---

# 📦 환경 변수 정리

## 📋 필수 환경 변수

### 🛢️ MySQL 설정
```
MYSQL_PORT=3307
MYSQL_HOST=localhost
MYSQL_ROOT_USERNAME=root
MYSQL_ROOT_PASSWORD=1234
MYSQL_DATABASE=ding_ga_ding
```

### 🍃 MongoDB 설정
```
MONGO_DB_HOST=localhost
MONGO_DB_PORT=27018
MONGO_INITDB_ROOT_USERNAME=root
MONGO_INITDB_ROOT_PASSWORD=1234
MONGO_INITDB_DATABASE=ding_ga_ding
```

### 🧠 Redis 설정
```
REDIS_HOST=localhost
REDIS_PORT=6370
REDIS_ROOT_PASSWORD=1234
```

### 🐇 RabbitMQ 설정
```
RABBIT_MQ_HOST=localhost
RABBIT_MQ_PORT=5673
RABBITMQ_DEFAULT_USER=root
RABBITMQ_DEFAULT_PASS=1234
```

### 🗂️ MinIO 설정
```
MINIO_HOST=localhost
MINIO_PORT=9001
MINIO_USERNAME=root
MINIO_PASSWORD=12341234
MINIO_BUCKET=original-mp3-audio-bucket
```

---

## 🧩 BACKEND 설정

```
# 공통
SPRING_ENV_MODE=DEVELOPMENT_LOCAL

# JPA
JPA_DDL_AUTO=create
JPA_SHOW_SQL=true
SQL_INIT_MODE=always
LOGGING_LEVEL=debug

# JWT
JWT_ACCESS_TOKEN_EXPIRE_TIME=100000
JWT_SECRET_KEY=${JWT_SECRET_KEY}

# Google OAuth2
GOOGLE_ID=${GOOGLE_ID}
GOOGLE_SECRET=${GOOGLE_SECRET}

# Redirect URI
REDIRECT_URI=http://localhost:8080/login
FRONT_REDIRECT_URI=http://localhost:3000/main

# OpenVidu
OPENVIDU_HOST=j12e107.p.ssafy.io
OPENVIDU_PORT=8443
OPENVIDU_SECRET=${OPENVIDU_SECRET}
```

---

## 🧠 AI 서비스 설정

```
# Celery 연결 포트
CELERY_BROKER_URL=redis://:1234@redis:6379
CELERY_RESULT_BACKEND=redis://:1234@redis:6379

```

---

## 🚀 배포 방법
[목차로 돌아가기](#-목차)

[배포.md](배포.md) 파일을 참고해주세요

로컬에서 개발환경을 실행하고 싶다면 아래 문서를 읽어주세요

---

### 💻 개발 환경 설정
개발 환경에서 서비스를 빌드하고 실행해볼 수 있는 방법을 알려드립니다.
오디오 저장 스토리지 (MiniO)는 지원하지않습니다.

#### ⚙️ 기본 개발 환경
전체 개발 환경(데이터베이스, 백엔드, 프론트엔드)을 실행하려면 아래 단계를 따라주세요:

> 🐳 **Docker가 설치되어 있어야 합니다!**  
> 설치되어 있지 않다면 [공식 Docker 페이지](https://www.docker.com/)에서 먼저 설치해주세요.  
> (권장: Docker Desktop 또는 Docker CLI + Docker Compose)

> 🐳 **/infra/env 폴더에 .env.development 파일을 넣어주세요**  
> .env.development 파일은 위에서 설명한 환경 변수 설정이 모두 들어가 있어야합니다.

---

#### 개발 서버 실행
```bash

# 도커 네트워크 생성, 데이터베이스 실행, API 코드 생성, 개발 서비스 실행
./development.sh
```

#### 목(Mock) API 서버로 개발
백엔드가 준비되지 않았을 때 Mock API 서버를 사용하여 개발하려면:

```bash

# 도커 네트워크 생성, API 코드 생성, Mock API 서버 실행
./development.mock.sh
```

#### API 코드만 재생성
OpenAPI 명세가 변경된 경우 코드만 재생성하려면:

```bash
# 이전 생성 코드 정리 및 API 코드 재생성
./codegen.sh
```

## 🔍 특이 사항 및 주의점
[목차로 돌아가기](#-목차)

### 📑 OpenAPI 코드 생성 프로세스
1. 🔄 `openapi-merger` 서비스가 API 스펙을 하나로 병합
2. 🔙 `spring-generator`가 백엔드 코드 생성
3. 🔝 `axios-generator`가 프론트엔드 API 클라이언트 코드 생성
4. 🧪 목(Mock) 서버 실행 시 `mock` 서비스가 병합된 OpenAPI 스펙을 기반으로 목(Mock) API 제공

### 🚪 포트 설정

#### 애플리케이션 서비스
- 백엔드(Spring): 🍃 8080
- 프론트엔드(Next.js): ⚛️ 3000
- 목(Mock) API 서버: 🧪 8081

#### 데이터베이스 및 서비스
- MySQL: 🐬 3307:3306
- MongoDB: 🍃 27018:27017
- Redis: 🔄 6370:6379
- RabbitMQ: 🐰 5673:5672, 관리 UI: 15673:15672
- MinIO: 📦 API: 9001, 콘솔: 9002

### 🚫 포트 충돌 해결
스크립트는 8080 포트(개발 환경) 또는 8081 포트(목(Mock) 서버) 사용 여부를 확인하고, 사용 중이면 알림을 제공합니다:
```
taskkill //F //PID $PID
```
명령으로 해당 프로세스를 종료할 수 있습니다(Windows 환경).

## 🐬 데이터베이스 정보
[목차로 돌아가기](#-목차)

### 🔐 MySQL 접속 정보
- **호스트**: mysql
- **포트**: 3306 (컨테이너 내부), 3307 (호스트 접근)
- **데이터베이스**: ding_ga_ding
- **루트 사용자**: root
- **루트 비밀번호**: 1234

### 🍃 MongoDB 접속 정보
- **호스트**: mongodb
- **포트**: 27017 (컨테이너 내부), 27018 (호스트 접근)
- **데이터베이스**: ding_ga_ding
- **루트 사용자**: root
- **루트 비밀번호**: 1234

### 🔄 Redis 접속 정보
- **호스트**: redis
- **포트**: 6379 (컨테이너 내부), 6370 (호스트 접근)
- **비밀번호**: 1234

### 🐰 RabbitMQ 접속 정보
- **호스트**: rabbitmq
- **포트**: 5672 (AMQP, 컨테이너 내부), 5673 (호스트 접근)
- **관리 UI 포트**: 15672 (컨테이너 내부), 15673 (호스트 접근)
- **사용자**: root
- **비밀번호**: 1234

### 📦 MinIO 접속 정보
- **호스트**: minio
- **API 포트**: 9001
- **콘솔 포트**: 9002
- **사용자**: root
- **비밀번호**: 12341234
- **브라우저 리다이렉트 URL**: http://localhost:9002
- **서버 URL**: http://localhost:9001


## 🛠️ 트러블슈팅
[목차로 돌아가기](#-목차)

### 🔧 일반적인 문제 해결
1. **🚫 컨테이너가 시작되지 않는 경우**:
    - 로그 확인: `docker logs <container_id>`
    - 포트 충돌 확인: `netstat -ano | grep ":8080"` 또는 `netstat -ano | grep ":8081"`

2. **❌ API 코드 생성 실패**:
    - OpenAPI 스펙 문법 확인
    - 로그 확인: `docker logs <openapi-merger-container-id>`

3. **🔌 데이터베이스 연결 오류**:
    - 환경 변수 설정 확인
    - 데이터베이스 컨테이너 상태 확인: `docker ps`
    - 도커 네트워크 확인: `docker network ls` (ding-ga-ding-network가 존재해야 함)

4. **⚠️ 생성된 코드가 올바르지 않은 경우**:
    - `./cleanup.sh` 실행 후 `./codegen.sh`로 다시 시도

5. **📊 목(Mock) 서버 문제**:
    - 포트 8081이 사용 가능한지 확인
    - OpenAPI 스펙 병합이 올바르게 되었는지 확인

[목차로 돌아가기](#-목차)
