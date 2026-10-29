# Stage 1: build the Spring Boot jar with the full JDK
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Gradle files first, so dependencies stay in the layer cache
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

COPY src src
RUN ./gradlew --no-daemon bootJar

# Stage 2: small runtime image with the JRE only
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl \
    && addgroup -S marua \
    && adduser -S -G marua marua
WORKDIR /app
COPY --from=builder --chown=marua:marua /workspace/build/libs/*.jar app.jar
USER marua

EXPOSE 8081
HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=5 \
    CMD curl -fs http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
