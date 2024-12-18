FROM gradle:7.6-jdk17 AS build

WORKDIR /app
COPY workspace/backend/ backend/

WORKDIR /app/backend
RUN ./gradlew clean build --no-daemon -x test

FROM openjdk:17-jdk

ENV SPRING_PROFILES_ACTIVE=prod

LABEL creator="wjdtmfgh@gmail.com"

WORKDIR /app

COPY --from=build /app/backend/backend-api/build/libs/backend-api-1.0.0.jar backend-api-1.0.0.jar