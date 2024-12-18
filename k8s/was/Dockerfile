FROM gradle:7.6-jdk17 AS build

ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

COPY . .

RUN cd workspace
RUN ls -la

RUN cd workspace && ./gradlew clean build --no-daemon

FROM openjdk:17-jdk

LABEL creator="wjdtmfgh@gmail.com"

WORKDIR /app

COPY --from=build /app/backend-api/build/libs/backend-api-1.0.0.jar backend-api-1.0.0.jar

EXPOSE 8080/tcp

CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-DRDS_USERNAME=${RDS_USERNAME}", "-DRDS_PW=${RDS_PW}", "-jar", "/app/backend-api-1.0.0.jar"]
