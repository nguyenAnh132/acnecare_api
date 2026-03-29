
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app


COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline


COPY src ./src
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy
WORKDIR /app


RUN mkdir -p /app/uploads


COPY --from=build /app/target/*.jar app.jar


EXPOSE 9090

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]