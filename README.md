# CodeGen 프로젝트 가이드 📚

## Docker Compose 실행 옵션 🐳

docker-compose 파일이 여러개 구성되어있는데 각각에 대해서 설명하겠습니다.

** 이곳에 설명 작성**

development 환경: 개발환경 (포트가 모두 개방, )


### 생성된 코드 정리하기
``` bash

./cleanup.sh

```


### 개발 환경 (Development) ⚙️

**`docker-compose.dev.yaml`**
```bash

docker-compose -f docker-compose.dev.yaml up --build --force-recreate
```

- 로컬 MySQL, Spring, Next.js를 한번에 실행
- 포트 개방: 
  - MySQL: 3306
  - Spring: 8080
  - Next.js: 3000
- 각 서비스에 직접 접근 가능

### 로컬 프로덕션 환경 (Local Production) 🔄

**`docker-compose.prod.local.yaml`**
```bash

docker-compose -f docker-compose.prod.local.yaml up --build --force-recreate
```

- 로컬 MySQL, Spring, Next.js를 한번에 실행
- 외부 포트 폐쇄 (보안 강화)
- Nginx를 통해 리버스 프록시 설정
- `localhost` URL로만 접근 가능

### 프로덕션 환경 (Production) 🚀

**`docker-compose.prod.yaml`**
```bash

docker-compose -f docker-compose.prod.yaml up --build --force-recreate
```

- Docker Hub에서 이미지를 다운로드하여 실행
- 외부 포트 폐쇄 (보안 강화)
- Nginx를 통해 리버스 프록시 설정
- `localhost` URL로만 접근 가능

## 유용한 Docker Compose 명령어 ⌨️

### 서비스 시작 시 추천 플래그

```bash

--build          # 컨테이너 시작 전 이미지 다시 빌드
--force-recreate # 컨테이너를 강제로 다시 생성
--no-cache       # 캐시 없이 새롭게 빌드
```
### 여러 개의 특정 서비스 빌드
``` bash

docker-compose build next
```

### 서비스 종료하기

```bash

docker-compose down
```

## OpenAPI Generator 사용 가이드 📝

CodeGen 프로젝트는 OpenAPI 명세서를 기반으로 코드를 자동 생성합니다.

```bash

docker-compose up
```

위 명령어를 실행하면 다음 위치에 API 코드가 생성됩니다:
- `backend/generated`: 백엔드 API 코드
- `frontend/generated`: 프론트엔드 API 코드

## 주의사항 ⚠️

- 각 환경에 맞는 Docker Compose 파일을 선택하여 실행하세요.
- 프로덕션 환경에서는 보안을 위해 직접적인 포트 접근이 차단됩니다.
- OpenAPI Generator를 통해 API 코드가 자동 생성되므로, API 변경 시 재생성이 필요합니다.

## 시스템 요구사항 💻

- Docker 및 Docker Compose 설치
- 개발 환경: 포트 3306, 8080, 3000 사용 가능
- 프로덕션 환경: 포트 80 사용 가능 (Nginx)

---

문의사항이 있으시면 프로젝트 관리자에게 연락해 주세요. 😊
