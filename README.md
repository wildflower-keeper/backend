# ☘ 들꽃가드닝 Backend
## 1. API 실행가이드
### 1.1. build
```shell
SPRING_PROFILES_ACTIVE=local ADMIN_PASSWORD=example RDS_USERNAME=example RDS_PW=example ./gradlew clean build
```
- SPRING_PROFILES_ACTIVE : local, prod; 로컬에서 실행 시 local 로 지정
- ADMIN_PASSWORD : 들꽃지기 관리자 계정 비밀번호; 로컬에서 실행시 임의로 지정
- RDS_USERNAME : DB username; 로컬에서 실행시 로컬 RDS username 입력
- RDS_PW : DB password; 로컬에서 실행시 로컬 RDS password 입력

`/backend-api/build/libs`에 backend-api-1.0.0.jar 생성되었으면 성공  

### 1.2. 실행
프로젝트 루트 경로에서 아래 명령 실행
```shell
java -Dspring.profiles.active="local" -DRDS_USERNAME="example" -DRDS_PW="example" -jar /backend-api/build/libs/backend-api-1.0.0.jar
```
- 맨 마지막(/backend-api/build/libs/backend-api-1.0.0.jar)은 1.1.에서 빌드한 jar 파일이 와야함
- spring.profiles.active : 1.1.의 SPRING_PROFILES_ACTIVE와 동일
- RDS_USERNAME, RDS_PW : 1.1.의 RDS_USERNAME, RDS_PW와 동일

### 1.3. swagger 접속
로컬호스트 실행시
```shell
http://localhost:8080/swagger-ui/index.html
```

## 2. 용어정의
- 계정
  - Shelter : 센터 관리자 계정
  - Homeless : 노숙인 계정
  - ShelterPublic : 센터 노숙인 공용 계정 (디바이스 별로 관리)
- 외출 외박
  - Outing : 외출 외박
  - Sleepover : 외박
  - Daytime Outing : 외출
