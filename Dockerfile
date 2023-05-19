FROM openjdk:11

ARG IMAGE_VERSION="1.0"
ARG NAME_OF_ACTUAL_JAR=app.jar
ARG ACCESS_PORT=8080
ARG JAR_FILE=./target/*.jar

LABEL "ua.foxminded.asharov"="AnatoliySharov's practice in FoxminEd"
LABEL ua.foxminded.asharov.carmanager="car-rest-service"
LABEL version=${IMAGE_VERSION}
LABEL description="Task4.5/dokerization with all infrastructure spring boot maven plugin"

WORKDIR /app

COPY ${JAR_FILE} ${NAME_OF_ACTUAL_JAR}

EXPOSE ${ACCESS_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
