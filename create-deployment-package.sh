#!/bin/bash

echo "================================================"
echo "HelloBooks QA Automation - Mac/Linux Deployment"
echo "================================================"

DEPLOYMENT_DIR="HelloBooks-QA-Deployment"
SOURCE_DIR=$(dirname "$0")

echo
echo "[1/5] Creating deployment directory..."
if [ -d "$DEPLOYMENT_DIR" ]; then
    rm -rf "$DEPLOYMENT_DIR"
fi
mkdir "$DEPLOYMENT_DIR"

echo
echo "[2/5] Copying project files..."
cp -r "$SOURCE_DIR"/* "$DEPLOYMENT_DIR/" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "Error: Failed to copy files"
    exit 1
fi

echo
echo "[3/5] Creating setup scripts..."

# Create Mac setup script
cat > "$DEPLOYMENT_DIR/setup-mac.sh" << 'EOF'
#!/bin/bash
echo "================================"
echo "HelloBooks QA Setup for macOS"
echo "================================"

echo
echo "[1/5] Checking system requirements..."

# Check Java
if command -v java &> /dev/null; then
    echo "✓ Java found: $(java -version 2>&1 | head -n 1)"
else
    echo "✗ Java not found"
    echo "Install with: brew install openjdk@11"
    echo "Or download from: https://adoptopenjdk.net/"
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    echo "✓ Maven found: $(mvn -version | head -n 1)"
else
    echo "✗ Maven not found"
    echo "Install with: brew install maven"
    echo "Or download from: https://maven.apache.org/download.cgi"
    exit 1
fi

# Check if Homebrew is available (optional)
if command -v brew &> /dev/null; then
    echo "✓ Homebrew available"
else
    echo "ℹ Homebrew not found (optional)"
fi

echo
echo "[2/5] Setting up Chrome (if needed)..."
if [ -d "/Applications/Google Chrome.app" ]; then
    echo "✓ Chrome found"
else
    echo "⚠ Chrome not found - WebDriverManager will handle this"
fi

echo
echo "[3/5] Making scripts executable..."
chmod +x *.sh

echo
echo "[4/5] Installing dependencies (may take 5-10 minutes first time)..."
mvn clean compile -U
if [ $? -ne 0 ]; then
    echo "✗ Setup failed. Check network connection and try again."
    exit 1
fi

echo
echo "[5/5] Running verification test..."
mvn test -Dtest=LoginTests#testPasswordMasking -Dheadless=true

echo
echo "================================"
echo "✓ Setup Complete!"
echo "================================"
echo
echo "Next steps:"
echo "  1. Run all tests: mvn test"
echo "  2. Run specific test: mvn test -Dtest=LoginTests"
echo "  3. View reports: target/reports/"
echo
EOF

# Create Linux setup script
cat > "$DEPLOYMENT_DIR/setup-linux.sh" << 'EOF'
#!/bin/bash
echo "================================"
echo "HelloBooks QA Setup for Linux"
echo "================================"

echo
echo "[1/6] Updating package lists..."
sudo apt update

echo
echo "[2/6] Checking/Installing Java..."
if command -v java &> /dev/null; then
    echo "✓ Java found: $(java -version 2>&1 | head -n 1)"
else
    echo "Installing Java 11..."
    sudo apt install openjdk-11-jdk -y
fi

echo
echo "[3/6] Checking/Installing Maven..."
if command -v mvn &> /dev/null; then
    echo "✓ Maven found: $(mvn -version | head -n 1)"
else
    echo "Installing Maven..."
    sudo apt install maven -y
fi

echo
echo "[4/6] Installing Chrome for testing..."
if command -v google-chrome &> /dev/null; then
    echo "✓ Chrome found"
else
    echo "Installing Chrome..."
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
    sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
    sudo apt update
    sudo apt install google-chrome-stable -y
fi

echo
echo "[5/6] Installing project dependencies..."
mvn clean compile -U
if [ $? -ne 0 ]; then
    echo "✗ Setup failed. Check network connection and try again."
    exit 1
fi

echo
echo "[6/6] Running verification test..."
mvn test -Dtest=LoginTests#testPasswordMasking -Dheadless=true

echo
echo "================================"
echo "✓ Setup Complete!"
echo "================================"
echo
echo "Next steps:"
echo "  1. Run all tests: mvn test"
echo "  2. Run specific test: mvn test -Dtest=LoginTests"
echo "  3. View reports: target/reports/"
echo
EOF

# Create universal run script
cat > "$DEPLOYMENT_DIR/run-tests.sh" << 'EOF'
#!/bin/bash
echo "================================"
echo "HelloBooks QA - Test Execution"
echo "================================"

echo
echo "Select test suite to run:"
echo "[1] All Tests"
echo "[2] Login Tests Only"
echo "[3] Signup Tests Only" 
echo "[4] Onboarding Tests Only"
echo "[5] Single Test (Password Masking)"
echo "[6] Headless Mode (All Tests)"
echo

read -p "Enter your choice (1-6): " choice

echo
case $choice in
    1) mvn test ;;
    2) mvn test -Dtest=LoginTests ;;
    3) mvn test -Dtest=SignupTests ;;
    4) mvn test -Dtest=OnboardingTests ;;
    5) mvn test -Dtest=LoginTests#testPasswordMasking ;;
    6) mvn test -Dheadless=true ;;
    *) echo "Invalid choice"; exit 1 ;;
esac

echo
echo "================================"
echo "Test execution completed!"
echo "Check reports in: target/reports/"
echo "================================"
EOF

# Make scripts executable
chmod +x "$DEPLOYMENT_DIR"/*.sh

echo
echo "[4/5] Creating documentation..."
if [ -f "DEPLOYMENT_GUIDE.md" ]; then
    cp "DEPLOYMENT_GUIDE.md" "$DEPLOYMENT_DIR/"
fi
if [ -f "README.md" ]; then
    cp "README.md" "$DEPLOYMENT_DIR/"
fi

echo
echo "[5/5] Creating deployment README..."
cat > "$DEPLOYMENT_DIR/README-DEPLOYMENT.md" << 'EOF'
# HelloBooks QA Automation - Quick Start

## 🚀 Quick Setup Instructions

### macOS:
1. Extract this folder to any location
2. Open Terminal in this folder
3. Run: `chmod +x *.sh && ./setup-mac.sh`
4. Run tests with: `./run-tests.sh`

### Linux:
1. Extract this folder to any location  
2. Open Terminal in this folder
3. Run: `chmod +x *.sh && ./setup-linux.sh`
4. Run tests with: `./run-tests.sh`

### Requirements:
- Java 11+ (will be installed automatically on Linux)
- Maven 3.6+ (will be installed automatically on Linux)
- Internet connection (for first-time dependency download)

### Manual Commands:
```bash
# Install dependencies
mvn clean compile

# Run all tests
mvn test

# Run specific test suite
mvn test -Dtest=LoginTests

# Run single test
mvn test -Dtest=LoginTests#testPasswordMasking

# Run in headless mode
mvn test -Dheadless=true
```

### Configuration:
- Edit `src/test/resources/config/config.properties` for settings
- Change `base.url` for different environments
- Set `headless=true` for CI/CD environments

### Test Reports:
- HTML Reports: `target/reports/`
- Screenshots: `target/screenshots/`
- Logs: `target/logs/`

## 📞 Support
For issues, check the documentation files included in this package.
EOF

echo
echo "================================================"
echo "✓ Deployment package created successfully!"
echo "================================================"
echo
echo "Package location: $DEPLOYMENT_DIR/"
echo
echo "To deploy to another Mac/Linux PC:"
echo "1. Copy the entire '$DEPLOYMENT_DIR' folder"
echo "2. On target PC, run the appropriate setup script:"
echo "   - macOS: ./setup-mac.sh"
echo "   - Linux: ./setup-linux.sh"
echo "3. Use './run-tests.sh' to execute tests"
echo

read -p "Create TAR.GZ file for easier transfer? (y/n): " createtar
if [[ $createtar == "y" || $createtar == "Y" ]]; then
    tar -czf HelloBooks-QA-Automation.tar.gz "$DEPLOYMENT_DIR"
    if [ $? -eq 0 ]; then
        echo "✓ TAR.GZ file created: HelloBooks-QA-Automation.tar.gz"
    else
        echo "⚠ Could not create TAR.GZ file"
    fi
fi

echo
echo "Deployment package ready!"