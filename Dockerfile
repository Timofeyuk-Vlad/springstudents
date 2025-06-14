FROM openjdk:17-jdk-alpine
RUN apk add --no-cache bash coreutils
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src/ src/
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon && \
    cp build/libs/springstudents-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]