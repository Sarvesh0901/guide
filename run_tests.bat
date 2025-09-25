@echo off
echo ================================
echo HelloBooks QA Automation Suite
echo ================================

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo Maven found. Running tests...
    goto run_tests
) else (
    echo Maven not found. Please install Maven first.
    echo.
    echo Installation Instructions:
    echo 1. Download Maven from: https://maven.apache.org/download.cgi
    echo 2. Extract to C:\apache-maven-3.x.x
    echo 3. Add C:\apache-maven-3.x.x\bin to your PATH environment variable
    echo 4. Restart command prompt and run this script again
    echo.
    echo Alternative: Use the Maven Wrapper (if available) with ./mvnw
    pause
    exit /b 1
)

:run_tests
echo.
echo Cleaning and compiling project...
call mvn clean compile
if %errorlevel% neq 0 (
    echo Compilation failed. Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Running password masking test (simple validation)...
call mvn test -Dtest=LoginTests#testPasswordMasking -Dheadless=true
if %errorlevel% neq 0 (
    echo Test execution failed. Check the output above.
    pause
    exit /b 1
)

echo.
echo Running a simple login validation test...
call mvn test -Dtest=LoginTests#testEmptyEmailValidation -Dheadless=true

echo.
echo ================================
echo Test Execution Completed
echo ================================
echo.
echo Check the following for results:
echo - Console output above
echo - Reports: target\reports\
echo - Screenshots: target\screenshots\
echo - Logs: target\logs\
echo.
pause