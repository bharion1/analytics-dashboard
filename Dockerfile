# Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Verify JAR was created
RUN ls -la target/ && find target -name "*.jar" -type f

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/analytics-dashboard.jar app.jar

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/metrics/dashboard || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

