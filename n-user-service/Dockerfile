FROM openjdk:17
WORKDIR /app
COPY "target/n-user-service-0.0.1-SNAPSHOT.jar" /app/n-user-service.jar
COPY "src/main/resources/application.yml" /app/application.yml
ENTRYPOINT ["java","-jar","n-user-service.jar", "--spring.config.location=file:/app/application.yml"]