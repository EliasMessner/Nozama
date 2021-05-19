# 2-stage-build
# first stage
FROM maven:3.8-openjdk-11 AS build
COPY app /usr/src/app
# clean target directory and build jars
RUN mvn -f /usr/src/app/pom.xml clean install
RUN ls /usr/src/app/target

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /usr/jars
COPY --from=build /usr/src/app/target/app.jar .
CMD ["java", "-jar", "/usr/jars/app.jar"]