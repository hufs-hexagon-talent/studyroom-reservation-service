#   첫 번째 스테이지: 빌드 환경 구성

#   베이스 이미지로 Java 17버전이 포함된 Docker 이미지를 사용
FROM bellsoft/liberica-openjdk-alpine:17 as build

# 작업 디렉터리 설정
WORKDIR /home/app
ENV TZ=Asia/Seoul
# 소스 코드와 빌드 파일 복사
COPY ./study-room-reservation-system .

# Gradle을 사용해 애플리케이션 빌드 (테스트 코드 실행X)
RUN ./gradlew clean build -x test
# --------------------------------------------
# 두 번째 스테이지: 실행 환경 구성
FROM bellsoft/liberica-openjdk-alpine:17

# 작업 디렉터리 설정
WORKDIR /app

RUN apk update && \
    apk add --no-cache \
      fontconfig \
      ttf-dejavu && \
    rm -rf /var/cache/apk/*

# 2) JVM 헤드리스 모드 설정 (AWT 에서 그래픽 환경 없이 동작하도록)
ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=true"

# 첫 번째 스테이지에서 생성된 JAR 파일을 두 번째 스테이지로 복사
COPY --from=build /home/app/build/libs/*.jar app.jar

LABEL authors="hwangbyeonghun"

#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#   컨테이너가 사용할 포트를 설정, 이 경우에는 8080 포트를 사용
EXPOSE 8080

#   컨테이너가 실행될 때 기본적으로 실행될 명령어를 설정( Java 애플리케이션을 실행하는 명령어 )
ENTRYPOINT ["java","-jar","app.jar"]