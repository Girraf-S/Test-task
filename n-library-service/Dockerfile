FROM openjdk:17
WORKDIR /app
COPY "target/n-library-service-0.0.1-SNAPSHOT.jar" /app/n-library-service.jar
COPY "src/main/resources/application.yml" /app/application.yml
ENTRYPOINT ["java","-jar","n-library-service.jar", "--spring.config.location=file:/app/application.yml"]