# Usa OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia i file Maven
COPY pom.xml .
COPY src ./src

# Build con Maven
RUN ./mvnw clean package -DskipTests

# Avvio
CMD ["java", "-jar", "target/caring-caring.jar"]
