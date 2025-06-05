FROM openjdk:17-jdk-alpine
RUN apk add --no-cache bash
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src/ src/
RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar" ]