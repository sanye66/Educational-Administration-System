FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY edu-common ./edu-common
COPY edu-services/edu-ai-gateway ./edu-services/edu-ai-gateway
RUN mvn clean package -DskipTests -pl edu-services/edu-ai-gateway -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/edu-services/edu-ai-gateway/target/*.jar app.jar
EXPOSE 8095
ENTRYPOINT ["java", "-jar", "app.jar"]