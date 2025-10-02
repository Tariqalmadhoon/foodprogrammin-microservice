# Runtime-only (نحتاج JAR جاهز)
FROM eclipse-temurin:17-jre
WORKDIR /app

# انسخ الجار الناتج من المافن
COPY target/*.jar app.jar

EXPOSE 8084

# الاتصال بقاعدة MySQL على جهازك (Windows)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/notification_service_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect

ENTRYPOINT ["java","-jar","/app/app.jar"]
