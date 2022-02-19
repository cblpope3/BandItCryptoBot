FROM openjdk:17-alpine
ARG JAR_FILE=./target/CryptoBot-1.0.jar
COPY ${JAR_FILE} /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080/tcp