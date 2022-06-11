FROM maven:3.6.0-jdk-11-slim AS build

COPY ./ghost_stor/src /app/src
COPY ./ghost_stor/pom.xml /app

RUN mvn -f /app/pom.xml clean package

FROM openjdk:11-jre-slim

COPY --from=build /app/target/ghost-project.jar /usr/local/lib/ghost-project.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/ghost-project.jar"]