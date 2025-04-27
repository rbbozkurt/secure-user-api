# Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Copy only necessary files
COPY build/libs/springboot-demo-0.0.1-SNAPSHOT.jar app.jar

# Final small runtime image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/app.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
