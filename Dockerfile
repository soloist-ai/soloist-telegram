# Build stage
FROM eclipse-temurin:25-jdk-alpine AS build

RUN adduser -D myuser && \
    mkdir -p /usr/src/app && \
    chown myuser:myuser /usr/src/app

USER myuser
WORKDIR /usr/src/app

COPY --chown=myuser:myuser .mvn .mvn
COPY --chown=myuser:myuser mvnw mvnw

COPY --chown=myuser:myuser pom.xml pom.xml
COPY --chown=myuser:myuser soloist-telegram-bot/pom.xml soloist-telegram-bot/pom.xml
COPY --chown=myuser:myuser soloist-telegram-general/pom.xml soloist-telegram-general/pom.xml
COPY --chown=myuser:myuser soloist-telegram-model/pom.xml soloist-telegram-model/pom.xml
COPY --chown=myuser:myuser soloist-telegram-service/pom.xml soloist-telegram-service/pom.xml

RUN mkdir -p /home/myuser/.m2
COPY --chown=myuser:myuser settings.xml /home/myuser/.m2/settings.xml

RUN chmod +x ./mvnw && \
    ./mvnw -q -B -DskipTests dependency:go-offline || true

COPY --chown=myuser:myuser . .

RUN chmod +x ./mvnw && \
    ./mvnw -q -B clean package -DskipTests

# Run stage
FROM eclipse-temurin:25-jre-alpine

RUN adduser -D myuser && \
    mkdir -p /app && \
    chown myuser:myuser /app

USER myuser
WORKDIR /app

COPY --from=build --chown=myuser:myuser \
    /usr/src/app/soloist-telegram-service/target/*.jar \
    /app/soloist-telegram.jar

EXPOSE 8080

CMD ["java", \
    "--enable-native-access=ALL-UNNAMED", \
    "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
    "-Dspring.profiles.active=prod", \
    "-jar", "soloist-telegram.jar"]
