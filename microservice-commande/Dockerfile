FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=/build/target/microservice-commande-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]
