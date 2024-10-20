FROM openjdk:17-jdk
MAINTAINER nikamilon.com
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]