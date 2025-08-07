FROM eclipse-temurin:23-jdk AS build
WORKDIR /app

# Копируем pom.xml и src
COPY pom.xml .
COPY src ./src

# Собираем приложение с помощью Maven
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Используем отдельный образ для запуска
FROM eclipse-temurin:23-jre
WORKDIR /app

COPY --from=build /app/target/ozon-helper-*.jar ./app.jar

EXPOSE 1212

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]