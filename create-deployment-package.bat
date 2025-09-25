@echo off
echo ================================================
echo HelloBooks QA Automation - Deployment Package
echo ================================================

set DEPLOYMENT_DIR=HelloBooks-QA-Deployment
set SOURCE_DIR=%~dp0

echo.
echo [1/5] Creating deployment directory...
if exist "%DEPLOYMENT_DIR%" rmdir /s /q "%DEPLOYMENT_DIR%"
mkdir "%DEPLOYMENT_DIR%"

echo.
echo [2/5] Copying project files...
xcopy "%SOURCE_DIR%*" "%DEPLOYMENT_DIR%\" /E /I /H /Y >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Failed to copy files
    pause
    exit /b 1
)

echo.
echo [3/5] Creating setup scripts...

:: Create Windows setup script
(
echo @echo off
echo echo ================================
echo echo HelloBooks QA Setup for Windows
echo echo ================================
echo echo.
echo echo [1/4] Checking Java installation...
echo java -version
echo if %%errorlevel%% neq 0 ^(
echo     echo ✗ Java not found. Please install Java 11+ from: https://adoptopenjdk.net/
echo     pause
echo     exit /b 1
echo ^)
echo echo ✓ Java found
echo echo.
echo echo [2/4] Checking Maven installation...
echo mvn -version
echo if %%errorlevel%% neq 0 ^(
echo     echo ✗ Maven not found. Please install Maven from: https://maven.apache.org/download.cgi
echo     pause
echo     exit /b 1
echo ^)
echo echo ✓ Maven found
echo echo.
echo echo [3/4] Installing dependencies ^(this may take 5-10 minutes first time^)...
echo mvn clean compile -U
echo if %%errorlevel%% neq 0 ^(
echo     echo ✗ Setup failed. Check network connection and try again.
echo     pause
echo     exit /b 1
echo ^)
echo echo.
echo echo [4/4] Running verification test...
echo mvn test -Dtest=LoginTests#testPasswordMasking -Dheadless=true
echo echo.
echo echo ================================
echo echo ✓ Setup Complete!
echo echo ================================
echo echo Next steps:
echo echo   1. Run all tests: mvn test
echo echo   2. Run specific test: mvn test -Dtest=LoginTests
echo echo   3. View reports: target\reports\
echo echo.
echo pause
) > "%DEPLOYMENT_DIR%\setup-windows.bat"

:: Create run script
(
echo @echo off
echo echo ================================
echo echo HelloBooks QA - Test Execution
echo echo ================================
echo echo.
echo echo Select test suite to run:
echo echo [1] All Tests
echo echo [2] Login Tests Only
echo echo [3] Signup Tests Only
echo echo [4] Onboarding Tests Only
echo echo [5] Single Test ^(Password Masking^)
echo echo.
set /p choice="Enter your choice (1-5): "
echo.
if "%%choice%%"=="1" mvn test
if "%%choice%%"=="2" mvn test -Dtest=LoginTests
if "%%choice%%"=="3" mvn test -Dtest=SignupTests
if "%%choice%%"=="4" mvn test -Dtest=OnboardingTests
if "%%choice%%"=="5" mvn test -Dtest=LoginTests#testPasswordMasking
echo.
echo ================================
echo Test execution completed!
echo Check reports in: target\reports\
echo ================================
pause
) > "%DEPLOYMENT_DIR%\run-tests.bat"

echo.
echo [4/5] Creating documentation...
copy /Y "DEPLOYMENT_GUIDE.md" "%DEPLOYMENT_DIR%\" >nul 2>&1
copy /Y "README.md" "%DEPLOYMENT_DIR%\" >nul 2>&1
copy /Y "NETWORK_TROUBLESHOOTING.md" "%DEPLOYMENT_DIR%\" >nul 2>&1

echo.
echo [5/5] Creating deployment README...
(
echo # HelloBooks QA Automation - Quick Start
echo.
echo ## 🚀 Quick Setup Instructions
echo.
echo ### Windows:
echo 1. Extract this folder to any location
echo 2. Double-click `setup-windows.bat`
echo 3. Wait for setup to complete
echo 4. Run tests with `run-tests.bat`
echo.
echo ### Requirements:
echo - Java 11+ ^(download from: https://adoptopenjdk.net/^)
echo - Maven 3.6+ ^(download from: https://maven.apache.org/download.cgi^)
echo - Internet connection ^(for first-time dependency download^)
echo.
echo ### Manual Commands:
echo ```bash
echo # Install dependencies
echo mvn clean compile
echo.
echo # Run all tests
echo mvn test
echo.
echo # Run specific test suite
echo mvn test -Dtest=LoginTests
echo.
echo # Run single test
echo mvn test -Dtest=LoginTests#testPasswordMasking
echo ```
echo.
echo ### Configuration:
echo - Edit `src/test/resources/config/config.properties` for settings
echo - Change `base.url` for different environments
echo - Set `headless=true` for CI/CD environments
echo.
echo ### Troubleshooting:
echo - Check `NETWORK_TROUBLESHOOTING.md` for network issues
echo - See `DEPLOYMENT_GUIDE.md` for detailed instructions
echo - Ensure Java and Maven are properly installed
echo.
echo ### Test Reports:
echo - HTML Reports: `target/reports/`
echo - Screenshots: `target/screenshots/`
echo - Logs: `target/logs/`
echo.
echo ## 📞 Support
echo For issues, check the documentation files included in this package.
) > "%DEPLOYMENT_DIR%\README-DEPLOYMENT.md"

echo.
echo ================================================
echo ✓ Deployment package created successfully!
echo ================================================
echo.
echo Package location: %DEPLOYMENT_DIR%\
echo.
echo To deploy to another PC:
echo 1. Copy the entire '%DEPLOYMENT_DIR%' folder
echo 2. On target PC, run 'setup-windows.bat'
echo 3. Use 'run-tests.bat' to execute tests
echo.
echo Optional: Create ZIP file for easier transfer
set /p createzip="Create ZIP file? (y/n): "
if /i "%createzip%"=="y" (
    powershell -Command "Compress-Archive -Path '%DEPLOYMENT_DIR%\*' -DestinationPath 'HelloBooks-QA-Automation.zip' -Force"
    if %errorlevel% equ 0 (
        echo ✓ ZIP file created: HelloBooks-QA-Automation.zip
    ) else (
        echo ⚠ Could not create ZIP file
    )
)

echo.
pause