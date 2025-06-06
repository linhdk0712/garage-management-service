# ====== Stage 2: Build application ======
FROM gradle:8.5.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build --no-daemon

# ====== Stage 3: Runtime Stage (Secure & Lightweight) ======
FROM eclipse-temurin:17-jre-ubi9-minimal AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
