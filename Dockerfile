# Build stage
FROM eclipse-temurin:25-jdk-alpine AS build

# Создаём пользователя для безопасности
RUN adduser -D myuser && \
    mkdir -p /usr/src/app && \
    chown myuser:myuser /usr/src/app

WORKDIR /usr/src/app
USER myuser

# Копируем исходники
COPY --chown=myuser:myuser . .

# Собираем проект
RUN chmod +x ./mvnw && \
    ./mvnw clean package -DskipTests --settings settings.xml

# Run stage
FROM eclipse-temurin:25-jre-alpine

# Копируем собранный JAR
COPY --from=build /usr/src/app/soloist-telegram-service/target/*.jar /app/soloist-telegram.jar

# Безопасность: создаём пользователя
RUN adduser -D myuser && \
    mkdir -p /app && \
    chown myuser:myuser /app

USER myuser
WORKDIR /app

EXPOSE 8080
CMD ["java", \
    "--enable-native-access=ALL-UNNAMED", \
    "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
    "-Dspring.profiles.active=prod", \
    "-jar", "soloist-telegram.jar"]