FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Detectar pom.xml en raíz o subcarpeta (-maxdepth 2)
RUN set -e; \
    P=$( [ -f pom.xml ] && echo . || find . -maxdepth 2 -name pom.xml -print -quit | xargs -r dirname ); \
    if [ -z "$P" ]; then echo "No se encontró pom.xml"; exit 1; fi; \
    cd "$P"; mvn -B -DskipTests clean package; \
    cp target/*.jar /opt/app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /opt/app.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
