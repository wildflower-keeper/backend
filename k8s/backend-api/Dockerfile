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

EXPOSE 8080/tcp
CMD ["java", "-Dserver.port=8080", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-DRDS_USERNAME=${RDS_USERNAME}", "-DRDS_PW=${RDS_PW}", "-DADMIN_PASSWORD=${ADMIN_PASSWORD}", "-DMAIL_PW=${MAIL_PW}", "-DMAIL_USERNAME=${MAIL_USERNAME}", "-DTOKEN_SECRET=${TOKEN_SECRET}", "-DPROJECT_ID=${PROJECT_ID}", "-jar", "/app/backend-api-1.0.0.jar"]
