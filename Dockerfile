FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /Qu
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /application
COPY --from=builder /Qu/target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080