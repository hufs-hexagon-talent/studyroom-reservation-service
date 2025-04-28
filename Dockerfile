#   첫 번째 스테이지: 빌드 환경 구성

#   베이스 이미지로 Java 17버전이 포함된 Docker 이미지를 사용
#FROM bellsoft/liberica-openjdk-alpine:17 as build
FROM eclipse-temurin:17-jdk-jammy AS build

# Gradle 캐시 최적화(선택)
ARG GRADLE_USER_HOME=/gradle-cache
ENV GRADLE_USER_HOME=${GRADLE_USER_HOME}

# 작업 디렉터리 설정
WORKDIR /home/app
# 소스 코드와 빌드 파일 복사
COPY ./study-room-reservation-system .

# Gradle을 사용해 애플리케이션 빌드 (테스트 코드 실행X)
RUN ./gradlew clean build -x test
# --------------------------------------------
# 두 번째 스테이지: 실행 환경 구성
#FROM bellsoft/liberica-openjdk-alpine:17
FROM eclipse-temurin:17-jdk-jammy

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 작업 디렉터리 설정
WORKDIR /app

# 첫 번째 스테이지에서 생성된 JAR 파일을 두 번째 스테이지로 복사
COPY --from=build /home/app/build/libs/*.jar app.jar

LABEL authors="hwangbyeonghun"
#   컨테이너가 사용할 포트를 설정, 이 경우에는 8080 포트를 사용
EXPOSE 8080

#   컨테이너가 실행될 때 기본적으로 실행될 명령어를 설정( Java 애플리케이션을 실행하는 명령어 )
ENTRYPOINT ["java","-jar","app.jar"]