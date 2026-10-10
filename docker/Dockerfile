FROM eclipse-temurin:17-jre-jammy

EXPOSE 8080

ENV APP_HOME /usr/src/app

COPY target/blog-demo-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

WORKDIR $APP_HOME

ENTRYPOINT ["java", "-jar", "/usr/src/app/app.jar"]