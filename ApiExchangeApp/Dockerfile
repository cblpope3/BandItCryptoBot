FROM openjdk:17-alpine
ARG JAR_FILE=./target/api_exchange_app-0.2.0.jar
COPY ${JAR_FILE} /usr/src/api_app/api_app.jar
WORKDIR /usr/src/api_app
ENTRYPOINT ["java","-jar","api_app.jar"]
EXPOSE 8080/tcp