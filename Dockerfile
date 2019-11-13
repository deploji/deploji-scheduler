FROM openjdk:11-jdk-slim as builder
COPY . .
RUN ./gradlew --no-daemon test
RUN ./gradlew --no-daemon bootJar

FROM openjdk:11-jre-slim
VOLUME /tmp
COPY --from=builder build/libs/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
