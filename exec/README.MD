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

## 📋 쉘 스크립트 요약 테이블

| 스크립트 이름 | 설명 | 주요 기능 |
|--------------|------|-----------|
| `cleanup.sh` | 🧹 생성된 코드 파일 정리 | 백엔드(.java)와 프론트엔드(.ts) 생성 파일 삭제 |
| `codegen.sh` | 🏗️ API 코드 자동 생성 | 도커 중지, 파일 정리, OpenAPI 기반 코드 생성 |
| `development.mock.sh` | 🔄 목(Mock) 개발 환경 구성 | 네트워크 생성, Mock 서버 실행, 코드 생성 |
| `development.sh` | 💻 전체 개발 환경 구성 | 네트워크 생성, DB 서비스 실행, 코드 생성, 프론트엔드 및 백엔드 서비스 실행 |


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

## ⚙️ 환경 변수 설정
[목차로 돌아가기](#-목차)

프로젝트에서는 다음 환경 파일들을 사용합니다:

- 🟢 `./infra/env/.env.development`: 개발 환경용 환경 변수
- 🔴 `./infra/env/.env.production`: 프로덕션 배포용 환경 변수

### 📋 필수 환경 변수

#### MySQL 설정
```
MYSQL_ROOT_PASSWORD=1234
MYSQL_DATABASE=ding_ga_ding
```

#### MongoDB 설정
```
MONGO_INITDB_ROOT_USERNAME=root
MONGO_INITDB_ROOT_PASSWORD=1234
MONGO_INITDB_DATABASE=ding_ga_ding
```

#### Redis 설정
```
REDIS_PASSWORD=1234
```

#### RabbitMQ 설정
```
RABBITMQ_DEFAULT_USER=root
RABBITMQ_DEFAULT_PASS=1234
```

#### MinIO 설정
```
MINIO_ROOT_USER=root
MINIO_ROOT_PASSWORD=12341234
```

### 📱 프론트엔드 환경 변수
빌드 시에 다음 ARG 값들이 설정됩니다:

- `NEXT_PUBLIC_API_BASE_URL`: 백엔드 API 접근 URL (기본값: spring:8080)
- `NEXT_PUBLIC_ENV_MODE`: 환경 모드 (DEVELOP_LOCAL, PRODUCTION)

## 🚀 배포 방법
[목차로 돌아가기](#-목차)

### 💻 개발 환경 설정

#### 기본 개발 환경
전체 개발 환경(데이터베이스, 백엔드, 프론트엔드)을 실행하려면:

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

## 💌 도움이 필요하신가요?
[목차로 돌아가기](#-목차)

문제가 발생하거나 추가 도움이 필요하시면 언제든지 팀에 문의해주세요!
함께 해결해 나가겠습니다. 😊 항상 응원합니다! 🎉