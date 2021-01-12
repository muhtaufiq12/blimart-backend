FROM openjdk:8-jdk-alpine
WORKDIR /
ADD target/*.jar my-app.jar
EXPOSE 8080
CMD java -jar my-app.jar