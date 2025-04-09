FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml ./ 
COPY .env ./
COPY keystore.jks /keystore.jks

RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

COPY --from=builder /keystore.jks /keystore.jks

COPY --from=builder /app/target/notenhanh-0.0.1-SNAPSHOT.jar /app/notenhanh-0.0.1-SNAPSHOT.jar

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "notenhanh-0.0.1-SNAPSHOT.jar"]
