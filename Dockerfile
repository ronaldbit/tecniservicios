FROM amazoncorretto:21-alpine-jdk AS build

WORKDIR /app

COPY . .

RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV TZ="America/Lima"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]