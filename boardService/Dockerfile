FROM adoptopenjdk/openjdk11 AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:11-jdk-slim
VOLUME /tmp
EXPOSE 8085
ARG JAR_FILE=./build/libs/boardService-0.0.1-SNAPSHOT.jar//
COPY ${JAR_FILE} a802.jar
ENTRYPOINT  ["java", "-Duser.timezone=Asia/Seoul", "-jar", "a802.jar"]