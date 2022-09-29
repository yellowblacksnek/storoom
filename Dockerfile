FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify --fail-never
ADD . $HOME
RUN mvn package -DskipTests

FROM openjdk:17
ARG JAR_FILE=*.jar
COPY --from=build /usr/app/target/${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]