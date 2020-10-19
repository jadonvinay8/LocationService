FROM openjdk:11
ADD target/LocationAPI-0.0.1-SNAPSHOT.jar location-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "location-api.jar"]
