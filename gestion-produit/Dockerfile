FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/gestion-produit-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]