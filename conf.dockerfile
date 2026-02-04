# Usa OpenJDK + Maven
FROM maven:3.9.3-eclipse-temurin-21 AS build

WORKDIR /app

# Copia pom.xml e src
COPY pom.xml .
COPY src ./src

# Build con Maven globale
RUN mvn clean package -DskipTests

# Step finale
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Start
CMD ["java", "-jar", "app.jar"]
