FROM openjdk:8-alpine
WORKDIR /app
COPY ./src .
RUN javac Test.java
CMD ["java","Test"]