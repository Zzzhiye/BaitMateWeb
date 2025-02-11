FROM openjdk:21-jdk
WORKDIR /app

COPY target/BaitMateWeb-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/templates/ /app/templates/
ENTRYPOINT ["java", "-jar", "app.jar"]
