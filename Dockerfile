FROM adoptopenjdk/openjdk11:jre11u-ubuntu-nightly
MAINTAINER Shreyas Dange 'shreyas.dange22@gmail.com'
ADD ./target/registration-service.jar registration-service.jar
RUN sh -c 'touch registration-service.jar'
ENTRYPOINT ["java","-jar","registration-service.jar"]