# Use the official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -Pproduction

# Use a smaller base image for the final application
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built application from the previous stage
COPY --from=build /app/target/sports-medicine-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
