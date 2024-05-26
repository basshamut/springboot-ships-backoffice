FROM eclipse-temurin:21-jdk-alpine
COPY target/springboot-ships-backoffice-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]