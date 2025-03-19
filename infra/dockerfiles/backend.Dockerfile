# 빌드 스테이지
FROM openjdk:17-alpine AS builder
WORKDIR /app

COPY ./gradlew .
COPY ./gradle gradle
COPY ./build.gradle .
COPY ./settings.gradle .
COPY ./src src
COPY ./generated .

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew

RUN ./gradlew build -x test

# 실행 스테이지
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar dinggading.jar
ENTRYPOINT ["java", "-jar", "dinggading.jar"]
