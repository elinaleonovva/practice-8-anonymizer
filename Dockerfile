# Shared Dockerfile: the MODULE argument selects one of the four Maven modules.
FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /build

COPY pom.xml ./
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY dataset-service/pom.xml dataset-service/pom.xml
COPY export-service/pom.xml export-service/pom.xml
COPY anonymizer-service/pom.xml anonymizer-service/pom.xml
COPY api-gateway/src api-gateway/src
COPY dataset-service/src dataset-service/src
COPY export-service/src export-service/src
COPY anonymizer-service/src anonymizer-service/src

ARG MODULE
RUN mvn -q -pl ${MODULE} -am package -DskipTests \
    && cp ${MODULE}/target/*.jar /tmp/application.jar

FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=build /tmp/application.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --retries=10 --start-period=25s \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
