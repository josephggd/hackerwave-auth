FROM openjdk:17
MAINTAINER hackerwave.com
COPY target/auth-1.0.0.jar auth-1.0.0.jar
ENTRYPOINT ["java","-jar","/auth-1.0.0.jar"]