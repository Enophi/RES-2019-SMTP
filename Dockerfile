FROM openjdk:8
COPY MockMock.jar /opt/mock.jar
WORKDIR /opt
EXPOSE 25 8282
CMD ["java", "-jar","mock.jar"]