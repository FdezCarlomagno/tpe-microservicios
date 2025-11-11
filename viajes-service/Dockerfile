# Stage 1: build with Maven (use an image with Java 17 + Maven)
FROM maven:3.9.5-eclipse-temurin-17 as build
WORKDIR /app
# copy only maven-related files first for caching
COPY pom.xml .
# if multi-module, copy parent pom and modules
COPY .mvn .mvn
COPY mvnw .
# copy project
COPY src ./src
# build jar
RUN mvn -B -DskipTests clean package

# Stage 2: runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
# copy jar from build stage (assumes the jar name follows standard target/*.jar)
COPY --from=build /app/target/*.jar app.jar
# set env var to allow spring to pick port from SERVER_PORT env
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --server.port=${SERVER_PORT:-8080}"]
