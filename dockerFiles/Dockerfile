# Use OpenJDK as the base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY ../pom.xml /app
COPY ../src /app/src

# Install Maven dependencies and package the application
RUN apt-get update && apt-get install -y maven \
 && mvn clean package -DskipTests

# Expose the application port
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "target/manastudio-0.0.1-SNAPSHOT.jar"]
