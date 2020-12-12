FROM openjdk:11-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /analyzer-api-app.jar
ENTRYPOINT ["java", "-jar", "/analyzer-api-app.jar"]