# Multi-stage Dockerfile for Spring Boot app
FROM maven:3.9.5-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
# copy the jar from the build stage (assuming default target path)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
