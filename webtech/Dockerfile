# --- Build-Stage ---
FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar -x test --no-daemon

# --- Run-Stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ENV PORT=10000
COPY --from=build /app/build/libs/*-SNAPSHOT.jar /app/app.jar
EXPOSE 10000
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT} -jar /app/app.jar"]
