# Build-Stage
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Dependencies cachen
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Source kopieren und bauen
COPY src ./src
RUN mvn -q -DskipTests package

# Runtime-Stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# JAR kopieren
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]