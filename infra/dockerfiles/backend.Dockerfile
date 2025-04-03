# Multi-stage build for Spring Boot application
FROM openjdk:17-alpine AS base

WORKDIR /app

# Development stage for testing and development
FROM base as development
RUN apk add --no-cache gradle

WORKDIR /app/backend
COPY ./backend/gradlew .
COPY ./backend/gradle gradle
COPY ./backend/build.gradle .
COPY ./backend/settings.gradle .
COPY ./backend/src src
COPY ./backend/generated .

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew

# Cache dependencies
RUN ./gradlew dependencies --no-daemon || return 0

CMD ["./gradlew", "bootRun", "--no-daemon"]

# Test stage for running tests
FROM development as test
WORKDIR /app/backend
CMD ["./gradlew", "test", "--no-daemon"]

# Build stage for creating the JAR
FROM development as build
WORKDIR /app/backend
RUN ./gradlew bootJar --no-daemon

# Production stage with minimal footprint
FROM base as production

WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/backend/build/libs/*.jar app.jar

# Add a non-root user to run the application
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seou", "-jar", "/app/app.jar"]