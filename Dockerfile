FROM openjdk:25-ea-slim-bookworm AS base

COPY src/ src/
COPY gradle/ gradle/
COPY gradlew gradlew
COPY build.gradle build.gradle

RUN ./gradlew idea

FROM base AS withfiles

RUN mkdir out

FROM withfiles AS withdirs

RUN ./gradlew build

