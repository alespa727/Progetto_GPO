# Stage di build con Maven + JDK 21
FROM maven:3.9.3-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia pom.xml e src
COPY pom.xml .
COPY src ./src

# Build con Maven globale
RUN mvn clean package -DskipTests

# Stage finale con JDK leggero
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Start
CMD ["java", "-jar", "app.jar"]
