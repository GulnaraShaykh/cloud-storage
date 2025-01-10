# Используем официальный образ OpenJDK 8 на основе Alpine Linux
FROM openjdk:8-jdk-alpine

# Указываем автора
LABEL authors="home"

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем собранный JAR-файл в контейнер
COPY build/libs/cloud-storage-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
