FROM openjdk:25-ea-slim-bookworm AS base

RUN mkdir app

WORKDIR app/

COPY entrypoint entrypoint
COPY src/ src/
COPY gradle/ gradle/
COPY gradlew gradlew
COPY build.gradle build.gradle

FROM base AS copied_files

RUN ./gradlew build

FROM copied_files AS compiled

ENTRYPOINT [ "/app/entrypoint" ]