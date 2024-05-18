# ☘ 들꽃가드닝 Backend
## 1. 실행가이드
```shell
SPRING_PROFILES_ACTIVE=local ADMIN_PASSWORD=example RDS_USERNAME=example RDS_PW=example ./gradlew clean build
```
- SPRING_PROFILES_ACTIVE : local, prod; 로컬에서 실행 시 local 로 지정
- ADMIN_PASSWORD : 들꽃지기 관리자 계정 비밀번호; 로컬에서 실행시 임의로 지정
- RDS_USERNAME : DB username; 로컬에서 실행시 로컬 RDS username 입력
- RDS_PW : DB password; 로컬에서 실행시 로컬 RDS password 입력

## 2. 용어정의
- 계정
  - Shelter : 센터 관리자 계정
  - Homeless : 노숙인 계정
  - ShelterPublic : 센터 노숙인 공용 계정 (디바이스 별로 관리)
- 외출 외박
  - Outing : 외출 외박
  - Sleepover : 외박
  - Daytime Outing : 외출
