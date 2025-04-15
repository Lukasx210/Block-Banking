FROM eclipse-temurin:21-jdk-jammy AS builder

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml .
COPY eureka-server/pom.xml ./eureka-server/
COPY eureka-server/src ./eureka-server/src/

WORKDIR /app/eureka-server
# Force repackaging explicitly
RUN mvn clean package spring-boot:repackage -DskipTests

FROM eclipse-temurin:21-jre-jammy
RUN useradd -m -u 1001 eureka && \
    mkdir -p /app && \
    chown -R eureka:eureka /app
USER eureka
WORKDIR /app

COPY --from=builder /app/eureka-server/target/eureka-server-*.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]