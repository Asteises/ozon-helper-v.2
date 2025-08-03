FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend

# Копируем package.json и устанавливаем зависимости
COPY frontend/package*.json ./
RUN npm install

# Копируем исходники и собираем фронт
COPY frontend ./
RUN npm run build

# ---- Backend build ----
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app

# Копируем pom.xml и src
COPY pom.xml .
COPY src ./src

# Копируем билд фронта в static/miniapp
COPY --from=frontend-build /app/frontend/dist/ ./src/main/resources/static/miniapp

# Устанавливаем Maven и собираем
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn clean package -DskipTests

# ---- Runtime ----
FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/target/ozon-helper-*.jar ./app.jar
EXPOSE 1212
ENV SPRING_PROFILES_ACTIVE=dev
ENTRYPOINT ["java", "-jar", "app.jar"]


#FROM eclipse-temurin:23-jdk AS build
#WORKDIR /app
#
## Копируем pom.xml и src
#COPY pom.xml .
#COPY src ./src
#
## Собираем приложение с помощью Maven
#RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
#
## Копируем pom.xml и скачиваем зависимости (для кэширования)
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
## Копируем исходники и собираем проект
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Используем отдельный образ для запуска
#FROM eclipse-temurin:23-jre
#WORKDIR /app
#
## Копируем собранный JAR-файл
#COPY --from=build /app/target/ozon-helper-*.jar ./app.jar
#
#EXPOSE 1212
#
#ENV SPRING_PROFILES_ACTIVE=dev
#
#ENTRYPOINT ["java", "-jar", "app.jar"]