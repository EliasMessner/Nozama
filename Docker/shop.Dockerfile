# 2-stage-build
# first stage
FROM maven:3.8-openjdk-11 AS build
COPY shop-app /usr/src/shop-app
# clean target directory and build jars
RUN mvn -f /usr/src/shop-app/pom.xml clean install

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /usr/jars
COPY --from=build /usr/src/shop-app/target/shop-app-1.0-SNAPSHOT-jar-with-dependencies.jar .