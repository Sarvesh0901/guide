# HelloBooks QA Automation Suite

## Overview

This is a comprehensive UI automation suite for testing the Login and Signup flows of HelloBooks (finance/accounting SaaS application). The framework is built using Java, Selenium WebDriver, TestNG, and includes comprehensive reporting with ExtentReports and Allure.

## 🚀 Quick Start

### Prerequisites

- **Java 11 or higher** - [Download](https://adoptopenjdk.net/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Chrome Browser** (latest version recommended)
- **Git** - [Download](https://git-scm.com/)

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hellobooks-qa-automation
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Verify setup**
   ```bash
   mvn test -Dtest=LoginTests#testPasswordMasking
   ```

## 🏗️ Framework Architecture

### Project Structure
```
src/
├── main/java/com/hellobooks/automation/
│   ├── config/          # Configuration management
│   ├── pages/           # Page Object Model classes
│   ├── utils/           # Utility classes (WebDriver, Screenshots, etc.)
│   └── listeners/       # TestNG listeners for reporting
└── test/
    ├── java/com/hellobooks/automation/
    │   ├── tests/       # Test classes
    │   └── dataproviders/ # Test data providers
    └── resources/
        ├── config/      # Configuration files
        └── testdata/    # Test data files
```

### Key Components

- **Page Object Model**: Maintainable and reusable page objects
- **Configuration Management**: Centralized configuration via properties
- **WebDriver Factory**: Thread-safe driver management with auto-retry
- **Reporting**: Dual reporting with ExtentReports (HTML) and Allure
- **OTP Handling**: Configurable OTP verification (mock/external API)
- **Screenshot Capture**: Automatic screenshots on test failures
- **Retry Mechanism**: Automatic retry for flaky tests

## ⚙️ Configuration

### Main Configuration File: `src/test/resources/config/config.properties`

```properties
# Environment Configuration
base.url=https://dev.hellobooks.ai/
browser=chrome
headless=false

# Test Data Configuration
test.email.domain=@example.com
test.email.prefix=qa.automation
use.timestamp.in.email=true

# OTP Configuration (see OTP Handling section)
otp.mode=mock
otp.mock.value=123456

# Retry and Screenshot Configuration
retry.count=2
capture.screenshots=true
screenshot.on.failure=true
```

### Environment Override

You can override any configuration via system properties:

```bash
mvn test -Dbrowser=firefox -Dheadless=true -Dbase.url=https://staging.hellobooks.ai/
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
# Login tests only
mvn test -Dtest=LoginTests

# Signup tests only  
mvn test -Dtest=SignupTests

# Onboarding tests only
mvn test -Dtest=OnboardingTests
```

### Run Specific Test Case
```bash
mvn test -Dtest=LoginTests#testValidLogin
```

### Run with Different Browser
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=chrome
```

### Run in Headless Mode
```bash
mvn test -Dheadless=true
```

### Run with Custom Configuration
```bash
mvn test -Dbase.url=https://staging.hellobooks.ai/ -Dretry.count=3
```

## 🔐 OTP Verification Handling

The framework supports multiple OTP verification strategies:

### 1. Mock OTP (Default - Recommended for CI/CD)
```properties
otp.mode=mock
otp.mock.value=123456
```

### 2. External API Integration (MailSlurp, Mailinator, etc.)
```properties
otp.mode=external_api
external.otp.api.url=https://api.mailslurp.com/
external.otp.api.key=your-api-key
```

### 3. Manual Input (Interactive Testing)
```properties
otp.mode=manual_input
```

### Implementation Details

The `OTPHandler` class provides:
- Automatic OTP retrieval based on configured mode
- Email verification simulation for testing
- Validation of OTP format
- Delivery delay simulation

Example usage in tests:
```java
String otp = OTPHandler.getOTP(email);
verificationPage.verifyWithCode(otp);
```

## 📊 Test Reporting

### ExtentReports (HTML)
- **Location**: `target/reports/HelloBooks_TestReport_[timestamp].html`
- **Features**: 
  - Interactive HTML dashboard
  - Screenshots on failures
  - Test execution timeline
  - Environment information
  - Pass/fail statistics

### Allure Reports
```bash
# Generate Allure report
mvn allure:report

# Open Allure report
mvn allure:serve
```

### Screenshots
- **Automatic capture** on test failures
- **Location**: `target/screenshots/`
- **Naming**: `[testName]_[timestamp].png`

## 📋 Test Cases Coverage

### High Priority Tests (P1) - 13 Automated Tests
- **Login**: Valid login, invalid password, unregistered email, field validations, logout
- **Signup**: Happy path, required field validations, email format, password policy
- **Onboarding**: Organization setup, completion flow

### Medium Priority Tests (P2) - 12 Automated Tests  
- **Login**: Remember me functionality, session validation
- **Signup**: Duplicate email, password confirmation, security validations
- **Onboarding**: Field validations, step navigation, optional steps

### Security & Boundary Tests - 5 Automated Tests
- XSS protection testing
- Input boundary validation
- Password masking verification
- Long input handling

### Total: 30+ Automated Test Cases

## 🏃‍♂️ CI/CD Integration

### GitHub Actions Example
```yaml
name: HelloBooks QA Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
      - run: mvn test -Dheadless=true
      - uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-reports
          path: target/reports/
```

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Dheadless=true'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/reports',
                        reportFiles: '*.html',
                        reportName: 'Test Report'
                    ])
                }
            }
        }
    }
}
```

## 🔧 Troubleshooting

### Common Issues

#### 1. WebDriver Issues
```bash
# Clear WebDriver cache
rm -rf ~/.cache/selenium/

# Update drivers manually
mvn dependency:resolve
```

#### 2. Element Not Found
- Check if locators need updating for the application
- Increase wait times in `config.properties`:
  ```properties
  explicit.wait=45
  implicit.wait=15
  ```

#### 3. Tests Failing in Headless Mode
- Some elements might behave differently in headless mode
- Try with `headless=false` first to debug
- Check viewport size settings

#### 4. OTP Verification Issues
- Verify `otp.mode` configuration
- For external API, check API credentials
- Ensure mock OTP value matches application expectations

### Browser-Specific Issues

#### Chrome
- Update to latest version
- Disable extensions: `--disable-extensions`
- Use incognito mode: `incognito=true`

#### Firefox
- Ensure GeckoDriver compatibility
- Check Firefox version compatibility

## 📝 Test Data Management

### Email Generation
- **Format**: `qa.automation+[timestamp]@example.com`
- **Uniqueness**: Timestamp-based to avoid conflicts
- **Customizable**: Via configuration properties

### Password Generation
- **Valid passwords**: Meet common policy requirements (8+ chars, mixed case, numbers, special chars)
- **Weak passwords**: For negative testing
- **Configurable**: Via `TestDataGenerator` class

### Test Users
For manual testing or data setup:
```properties
# Primary test user (to be created manually if needed)
primary.test.email=qa.automation+primary@example.com
primary.test.password=ValidPass123!
```

## 🔄 Maintenance

### Adding New Tests
1. Create test method in appropriate test class
2. Add corresponding test case to documentation
3. Update priority and description annotations
4. Ensure proper cleanup in teardown

### Updating Locators
1. Inspect application elements
2. Update locators in respective Page Object classes
3. Test changes locally before committing

### Framework Updates
1. Update dependencies in `pom.xml`
2. Test compatibility with new versions
3. Update documentation if configuration changes

## 📞 Support

### Framework Documentation
- **JavaDoc**: Generate with `mvn javadoc:javadoc`
- **Code Coverage**: `mvn jacoco:report`

### Debugging
1. **Enable debug logging**: Set `log.level=DEBUG` 
2. **Visual debugging**: Run with `headless=false`
3. **Step-by-step**: Add breakpoints in IDE
4. **Screenshots**: Check `target/screenshots/` for failure captures

---

## 📈 Execution Summary

After running the complete test suite, you should see:
- **Total Tests**: 30+
- **Coverage**: Login, Signup, Onboarding, Security, Validation
- **Reports**: HTML dashboard + Allure reports
- **Screenshots**: Captured on failures
- **Execution Time**: ~5-10 minutes (depending on application response times)

For questions or issues, please refer to the test execution logs and generated reports.