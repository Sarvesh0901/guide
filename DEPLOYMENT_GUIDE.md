# 🚀 HelloBooks QA Automation - Deployment Guide

## 📦 How to Deploy and Run on Different PCs

### 🎯 **Deployment Overview**
This guide shows you how to set up and run the HelloBooks QA Automation framework on any Windows, Mac, or Linux PC.

---

## 📋 **Prerequisites for Target PC**

### **Required Software:**
1. **Java 11 or higher** - [Download](https://adoptopenjdk.net/)
2. **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
3. **Git** (optional) - [Download](https://git-scm.com/)
4. **Chrome Browser** (recommended) - Auto-downloaded by WebDriverManager

### **Optional Software:**
- **IntelliJ IDEA** or **Eclipse** (for development)
- **Firefox/Edge** (for cross-browser testing)

---

## 📁 **Method 1: Complete Project Transfer**

### **Step 1: Package the Project**
On the source PC, create a deployment package:

```bash
# Create deployment folder
mkdir HelloBooks-Deployment
cd HelloBooks-Deployment

# Copy entire project
xcopy "C:\Users\sarve\Desktop\New folder (8)" . /E /I

# Optional: Create a ZIP file
powershell Compress-Archive -Path * -DestinationPath HelloBooks-QA-Automation.zip
```

### **Step 2: Transfer to Target PC**
- **USB Drive**: Copy the folder/ZIP to USB and transfer
- **Cloud Storage**: Upload to Google Drive, OneDrive, or Dropbox
- **Network Share**: Copy over network
- **Email**: Send ZIP file (if under size limit)
- **Git Repository**: Push to GitHub/GitLab (recommended)

### **Step 3: Setup on Target PC**
```bash
# Extract if ZIP file
# Navigate to project directory
cd HelloBooks-QA-Automation

# Install dependencies and compile
mvn clean compile

# Run tests
mvn test
```

---

## 🌐 **Method 2: Git Repository Deployment (Recommended)**

### **Step 1: Create Git Repository**
On source PC:
```bash
# Initialize git repository
cd "C:\Users\sarve\Desktop\New folder (8)"
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - HelloBooks QA Automation Framework"

# Add remote repository (GitHub/GitLab)
git remote add origin https://github.com/yourusername/hellobooks-qa-automation.git

# Push to repository
git push -u origin main
```

### **Step 2: Clone on Target PC**
On target PC:
```bash
# Clone the repository
git clone https://github.com/yourusername/hellobooks-qa-automation.git

# Navigate to project
cd hellobooks-qa-automation

# Install dependencies
mvn clean compile

# Run tests
mvn test
```

---

## 🖥️ **Method 3: Platform-Specific Deployment**

### **🪟 Windows Deployment**

**Create Deployment Package:**
```batch
@echo off
echo Creating HelloBooks Deployment Package...

:: Create deployment folder
mkdir HelloBooks-Deployment
cd HelloBooks-Deployment

:: Copy project files
xcopy "C:\Users\sarve\Desktop\New folder (8)" project\ /E /I

:: Create setup script
echo @echo off > setup.bat
echo echo Setting up HelloBooks QA Automation... >> setup.bat
echo echo Checking Java installation... >> setup.bat
echo java -version >> setup.bat
echo if %%errorlevel%% neq 0 echo Please install Java 11+ and try again >> setup.bat
echo echo Checking Maven installation... >> setup.bat
echo mvn -version >> setup.bat
echo if %%errorlevel%% neq 0 echo Please install Maven and try again >> setup.bat
echo echo Installing dependencies... >> setup.bat
echo cd project >> setup.bat
echo mvn clean compile >> setup.bat
echo echo Setup complete! >> setup.bat
echo echo Run 'mvn test' to execute tests >> setup.bat
echo pause >> setup.bat

:: Create run script
echo @echo off > run-tests.bat
echo cd project >> run-tests.bat
echo mvn test >> run-tests.bat
echo pause >> run-tests.bat

echo Deployment package created!
```

### **🍎 macOS Deployment**

**Create Setup Script:**
```bash
#!/bin/bash
# setup-macos.sh

echo "Setting up HelloBooks QA Automation on macOS..."

# Check Java
if command -v java &> /dev/null; then
    echo "✓ Java found: $(java -version 2>&1 | head -n 1)"
else
    echo "⚠ Java not found. Install with: brew install openjdk@11"
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    echo "✓ Maven found: $(mvn -version | head -n 1)"
else
    echo "⚠ Maven not found. Install with: brew install maven"
    exit 1
fi

# Install dependencies
echo "Installing dependencies..."
mvn clean compile

echo "✓ Setup complete! Run 'mvn test' to execute tests"
```

### **🐧 Linux Deployment**

**Create Setup Script:**
```bash
#!/bin/bash
# setup-linux.sh

echo "Setting up HelloBooks QA Automation on Linux..."

# Install Java if not present
if ! command -v java &> /dev/null; then
    echo "Installing Java..."
    sudo apt update
    sudo apt install openjdk-11-jdk -y
fi

# Install Maven if not present
if ! command -v mvn &> /dev/null; then
    echo "Installing Maven..."
    sudo apt install maven -y
fi

# Setup Chrome for headless testing
if ! command -v google-chrome &> /dev/null; then
    echo "Installing Chrome..."
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
    sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
    sudo apt update
    sudo apt install google-chrome-stable -y
fi

# Install dependencies
echo "Installing project dependencies..."
mvn clean compile

echo "✓ Setup complete! Run 'mvn test' to execute tests"
```

---

## 🛠️ **Method 4: Docker Deployment (Advanced)**

### **Create Dockerfile:**
```dockerfile
FROM openjdk:11-jdk

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Install Chrome for testing
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list' && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    apt-get clean

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Install dependencies
RUN mvn clean compile

# Default command
CMD ["mvn", "test", "-Dheadless=true"]
```

### **Build and Run Docker:**
```bash
# Build Docker image
docker build -t hellobooks-qa .

# Run tests
docker run --rm hellobooks-qa

# Run with custom parameters
docker run --rm hellobooks-qa mvn test -Dtest=LoginTests
```

---

## 📝 **Deployment Checklist**

### **Pre-Deployment (Source PC):**
- [ ] Verify all tests run successfully
- [ ] Update configuration files if needed
- [ ] Create deployment package
- [ ] Document any environment-specific settings

### **Target PC Setup:**
- [ ] Install Java 11+
- [ ] Install Maven 3.6+
- [ ] Install Chrome browser
- [ ] Transfer project files
- [ ] Run setup script

### **Post-Deployment Verification:**
- [ ] Run `mvn clean compile` successfully
- [ ] Execute sample test: `mvn test -Dtest=LoginTests#testPasswordMasking`
- [ ] Verify reports are generated
- [ ] Check screenshots are captured on failures

---

## ⚙️ **Configuration for Different Environments**

### **Environment-Specific Settings:**

**Create `env-config.properties` for each environment:**

```properties
# Development Environment
base.url=https://dev.hellobooks.ai/
browser=chrome
headless=false

# Staging Environment  
base.url=https://staging.hellobooks.ai/
browser=chrome
headless=false

# CI/CD Environment
base.url=https://qa.hellobooks.ai/
browser=chrome
headless=true
```

### **Run with Different Environments:**
```bash
# Development
mvn test -Denv=dev

# Staging
mvn test -Denv=staging -Dbase.url=https://staging.hellobooks.ai/

# Headless mode
mvn test -Dheadless=true

# Different browser
mvn test -Dbrowser=firefox
```

---

## 🔧 **Troubleshooting Common Deployment Issues**

### **Issue 1: Java Version Mismatch**
```bash
# Check Java version
java -version

# Solution: Update JAVA_HOME
export JAVA_HOME=/path/to/java11
```

### **Issue 2: Maven Not Found**
```bash
# Check Maven installation
mvn -version

# Solution: Add Maven to PATH or reinstall
```

### **Issue 3: Network/Proxy Issues**
```bash
# Use corporate proxy settings
mvn test -Dhttps.proxyHost=proxy.company.com -Dhttps.proxyPort=8080
```

### **Issue 4: Permission Issues (Linux/Mac)**
```bash
# Make scripts executable
chmod +x setup-linux.sh
chmod +x run-tests.sh
```

---

## 📊 **CI/CD Integration**

### **GitHub Actions (`.github/workflows/tests.yml`):**
```yaml
name: HelloBooks QA Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        
    - name: Run tests
      run: mvn test -Dheadless=true
      
    - name: Upload test reports
      uses: actions/upload-artifact@v3
      if: always()
      with:
        name: test-reports
        path: target/reports/
```

### **Jenkins Pipeline:**
```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8'
        jdk 'JDK-11'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-repo/hellobooks-qa-automation.git'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn clean test -Dheadless=true'
            }
        }
        
        stage('Reports') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/reports',
                    reportFiles: '*.html',
                    reportName: 'Test Results'
                ])
            }
        }
    }
}
```

---

## 🎯 **Quick Start Commands for New PC**

### **Windows:**
```batch
# 1. Download and extract project
# 2. Open Command Prompt in project folder
# 3. Run:
mvn clean compile
mvn test -Dtest=LoginTests#testPasswordMasking
```

### **Mac/Linux:**
```bash
# 1. Download and extract project
# 2. Open Terminal in project folder
# 3. Run:
chmod +x *.sh
./setup-macos.sh  # or ./setup-linux.sh
mvn test -Dtest=LoginTests#testPasswordMasking
```

---

## 🏆 **Deployment Success Checklist**

After deployment, verify:
- [ ] ✅ Project compiles without errors
- [ ] ✅ Sample test runs successfully  
- [ ] ✅ Reports are generated in `target/reports/`
- [ ] ✅ Screenshots captured on failures
- [ ] ✅ All browsers work (Chrome, Firefox, Edge)
- [ ] ✅ Configuration can be modified easily
- [ ] ✅ Framework is ready for production use

**🎉 Congratulations! Your HelloBooks QA Automation framework is now deployed and ready to run on any PC!**