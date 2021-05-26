# 2-stage-build
# first stage
FROM maven:3.8-openjdk-11 AS build
COPY app /usr/src/app
# clean target directory and build jars
RUN mvn -f /usr/src/app/pom.xml clean install

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /usr/jars
COPY --from=build /usr/src/app/target/app-1.0-SNAPSHOT-jar-with-dependencies.jar .
CMD ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005", "app-1.0-SNAPSHOT-jar-with-dependencies.jar"]