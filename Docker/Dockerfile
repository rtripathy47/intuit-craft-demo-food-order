# Dockerfile
# Use a base image with Java
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/online-food-ordering-system-1.0.0.0.jar online-food-ordering-system-1.0.0.0.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "online-food-ordering-system-1.0.0.0.jar"]

