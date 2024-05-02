FROM alpine:latest

ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
RUN apk add openjdk17

# Has to be set explictly to find binaries
ENV PATH=$PATH:${JAVA_HOME}/bin

RUN apk add --no-cache tzdata
ENV TZ Asia/Jakarta

VOLUME logs

RUN apk add busybox-extras
RUN apk add curl

ADD /target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /gateway-service-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-jar","/gateway-service-0.0.1-SNAPSHOT.jar"]
LABEL maintainer = "mlutfiazizan@gmail.com"