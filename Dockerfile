# Stage 1: Build the dependencies then the app
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/ayKhana-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "ayKhana-0.0.1-SNAPSHOT.jar"]
