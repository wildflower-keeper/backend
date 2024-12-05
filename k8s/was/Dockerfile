FROM openjdk:17-jdk

LABEL creator="kiel0103@naver.com"
LABEL version="1.0.0"

WORKDIR /app
COPY backend-api/build/libs/backend-api-1.0.0.jar backend-api-1.0.0.jar

EXPOSE 8080/tcp

CMD ["java", "-jar", "/app/backend-api-1.0.0.jar"]