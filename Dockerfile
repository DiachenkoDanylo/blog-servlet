# Use a base image with Tomcat
FROM tomcat:9.0.75-jdk11-temurin

# Set the working directory
WORKDIR /usr/local/tomcat/webapps

# Copy your WAR file into the Tomcat webapps directory
COPY target/diet-blog-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose the Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]