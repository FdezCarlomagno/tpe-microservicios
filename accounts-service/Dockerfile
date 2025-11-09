FROM eclipse-temurin:21-jdk
WORKDIR /accounts-service
COPY target/accounts-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
