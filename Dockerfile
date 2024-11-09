# Стадия сборки
FROM eclipse-temurin:17-jdk-alpine AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только файлы, необходимые для установки зависимостей
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn/

# Кэшируем зависимости Maven
RUN ./mvnw dependency:go-offline

# Копируем исходный код
COPY src ./src

# Даем права на выполнение скрипта mvnw
RUN chmod +x ./mvnw

# Сборка приложения
RUN ./mvnw clean package -DskipTests

# Проверяем содержимое директории target
RUN ls -la /app/target/

# Стадия выполнения
FROM eclipse-temurin:17-jre-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar файл из предыдущего образа
COPY --from=build /app/target/*.jar /app/wallet-api.jar

# Даем права на выполнение JAR-файла
RUN chmod 755 /app/wallet-api.jar

# Устанавливаем переменную окружения для порта
ENV PORT=8080
EXPOSE ${PORT}

# Указываем команду для запуска приложения
CMD ["java", "-jar", "/app/wallet-api.jar"]
