FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/payment-service.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
