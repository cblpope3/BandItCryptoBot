FROM openjdk:17-alpine
ARG JAR_FILE=./target/bot_app-0.2.0.jar
COPY ${JAR_FILE} /usr/src/bot_app/bot_app.jar
WORKDIR /usr/src/bot_app
ENTRYPOINT ["java","-jar","bot_app.jar"]
EXPOSE 8080/tcp