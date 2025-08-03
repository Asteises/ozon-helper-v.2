# ---- Stage 1: Build frontend ----
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend

# Копируем package.json и устанавливаем зависимости
COPY frontend/package*.json ./
RUN npm install

# Копируем фронтенд код и билдим (файлы уйдут в ../src/main/resources/static/miniapp)
COPY frontend/ ./
WORKDIR /app
RUN npm run --prefix frontend build

# ---- Stage 2: Build backend ----
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app

# Копируем pom.xml и backend
COPY pom.xml .
COPY src ./src

# Устанавливаем Maven и собираем JAR
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn clean package -DskipTests

# ---- Stage 3: Runtime ----
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