FROM openjdk:25-ea-slim-bookworm AS base

COPY src/ src/
COPY gradle/ gradle/
COPY gradlew gradlew
COPY build.gradle build.gradle

RUN ./gradlew build