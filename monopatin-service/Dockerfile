FROM eclipse-temurin:21-jdk
WORKDIR /monopatin-service
COPY target/monopatin-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
