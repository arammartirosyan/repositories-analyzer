FROM openjdk:11-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /api-gateway-app.jar
ENTRYPOINT ["java", "-jar", "/api-gateway-app.jar"]