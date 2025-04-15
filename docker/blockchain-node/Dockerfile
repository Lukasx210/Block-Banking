FROM eclipse-temurin:21-jdk-jammy AS builder

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
# Copy parent POM first for dependency resolution
COPY pom.xml .
# Copy module POM and source
COPY blockchain-node/pom.xml ./blockchain-node/
COPY blockchain-node/src ./blockchain-node/src/

WORKDIR /app/blockchain-node
# Build with Spring Boot repackaging
RUN mvn clean package spring-boot:repackage -DskipTests

FROM eclipse-temurin:21-jre-jammy
RUN useradd -m -u 1001 blockchain && \
    mkdir -p /app && \
    chown -R blockchain:blockchain /app
USER blockchain
WORKDIR /app

# Copy specific JAR name pattern
COPY --from=builder /app/blockchain-node/target/blockchain-node-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]