FROM openjdk:17
ARG JAR_FILE=/localmood-api/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "/app.jar"]