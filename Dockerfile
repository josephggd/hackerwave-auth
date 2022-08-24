FROM openjdk:11
EXPOSE 8080
MAINTAINER hackerwave.com
COPY target/hw-auth-0.0.1-SNAPSHOT.jar hw-auth-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/hw-auth-0.0.1-SNAPSHOT.jar"]