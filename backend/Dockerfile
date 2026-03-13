FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-ea-17-slim-buster
WORKDIR /app
COPY --from=build ./app/target/librus-0.0.1-SNAPSHOT.jar ./librus-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","./librus-0.0.1-SNAPSHOT.jar"]
