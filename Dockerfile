FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/ayKhana-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "ayKhana-0.0.1-SNAPSHOT.jar"]
