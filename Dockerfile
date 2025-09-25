# Use official OpenJDK image
FROM openjdk:11-jdk-slim

# Set maintainer
LABEL maintainer="HelloBooks QA Team"
LABEL description="HelloBooks QA Automation Framework"

# Install system dependencies
RUN apt-get update && \
    apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list' && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Maven configuration first (for better caching)
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
COPY src/test/resources ./src/test/resources

# Compile the project
RUN mvn clean compile

# Set environment variables
ENV MAVEN_OPTS="-Xmx512m"
ENV JAVA_OPTS="-Xmx512m"

# Create volume for reports
VOLUME ["/app/target/reports", "/app/target/screenshots", "/app/target/logs"]

# Default command - run all tests in headless mode
CMD ["mvn", "test", "-Dheadless=true"]

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD mvn --version || exit 1

# Expose port for potential web interface (future enhancement)
EXPOSE 8080

# Add entrypoint script for flexibility
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh
ENTRYPOINT ["docker-entrypoint.sh"]