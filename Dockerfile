FROM openjdk:8-jre-alpine

LABEL Renan Calmon

ENV JAVA_OPTS ""
ENV APP_HOME /app

RUN mkdir ${APP_HOME}
WORKDIR ${APP_HOME}

COPY target/*.jar ${APP_HOME}/app.jar
COPY newrelic/ ${APP_HOME}/newrelic/

EXPOSE 8090
ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar