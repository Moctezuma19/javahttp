FROM openjdk:8-alpine
WORKDIR /app
COPY ./src .
RUN javac -cp .:fusionauth-jwt-3.5.3.jar:jackson-annotations-2.10.3.jar:jackson-core-2.10.3.jar:jackson-databind-2.10.3.jar Test.java
CMD ["java","-cp",".:fusionauth-jwt-3.5.3.jar:jackson-annotations-2.10.3.jar:jackson-core-2.10.3.jar:jackson-databind-2.10.3.jar","Test"]