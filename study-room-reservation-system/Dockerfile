#   베이스 이미지로 Java 17버전이 포함된 Docker 이미지를 사용
FROM bellsoft/liberica-openjdk-alpine:17

#   우분투 최신 버전
FROM ubuntu:latest

#	Gradle을 사용해 빌드를 실행하는 명령어
CMD ["./gradlew", "clean", "build"]

#   컨테이너 내에 /tmp 디렉터리를 볼륨으로 설정
VOLUME /tmp

#   Gradle로 빌드한 jar 파일의 위치를 변수로 설정
ARG JAR_FILE=build/libs/*.jar

LABEL authors="hwangbyeonghun"

#   JAR_FILE 변수에 지정된 파일을 app.jar라는 이름으로 컨테이너에 추가
COPY ${JAR_FILE} app.jar

#   컨테이너가 사용할 포트를 설정, 이 경우에는 8080 포트를 사용
EXPOSE 8080

#   컨테이너가 실행될 때 기본적으로 실행될 명령어를 설정, 이 경우에는 Java 애플리케이션을 실행하는 명령어
ENTRYPOINT ["java","-jar","/app.jar"]