# # Use OpenJDK 21 as base image
# FROM eclipse-temurin:21-jdk

# # Set the working directory
# WORKDIR /app

# # Copy everything
# COPY . .

# # Make Maven wrapper executable
# RUN chmod +x mvnw

# # Build the application (optional – useful for multi-stage, see below)
# # RUN ./mvnw clean package -DskipTests

# # Expose application port (e.g., 8080)
# EXPOSE 8080

# # Run the app using Maven wrapper
# CMD ["./mvnw", "spring-boot:run"]

# Use OpenJDK 21 as base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml to cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Pre-download dependencies (only reruns if pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the project
COPY . .

# Expose port
EXPOSE 8080

# Run application
CMD ["./mvnw", "spring-boot:run"]
