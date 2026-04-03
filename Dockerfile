FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml ./
RUN mvn -B -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8089

# Render provides PORT dynamically; fallback keeps local behavior.
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8089} -jar /app/app.jar"]