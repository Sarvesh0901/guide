@echo off
echo ========================================
echo HelloBooks QA Automation - Network Fix
echo ========================================

echo.
echo [1/6] Testing Internet Connectivity...
ping -n 2 8.8.8.8 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Internet connection is working
) else (
    echo ✗ No internet connection detected
    echo   Please check your network connection
    pause
    exit /b 1
)

echo.
echo [2/6] Testing Maven Repository Access...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/' -UseBasicParsing -TimeoutSec 10 | Out-Null; Write-Host '✓ Maven Central is accessible' } catch { Write-Host '✗ Cannot access Maven Central' }"

echo.
echo [3/6] Checking Maven Installation...
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo ✓ Maven is installed
    mvn -version
) else (
    echo ✗ Maven not found
    echo   Please install Maven first: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo.
echo [4/6] Setting up Maven configuration...
if not exist "%USERPROFILE%\.m2" mkdir "%USERPROFILE%\.m2"
copy /Y "maven-settings.xml" "%USERPROFILE%\.m2\settings.xml" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Maven settings configured
) else (
    echo ⚠ Could not copy Maven settings (may already exist)
)

echo.
echo [5/6] Clearing Maven cache...
echo   This may take a moment...
if exist "%USERPROFILE%\.m2\repository" (
    rmdir /s /q "%USERPROFILE%\.m2\repository" >nul 2>&1
    echo ✓ Maven cache cleared
) else (
    echo ✓ No cache to clear
)

echo.
echo [6/6] Attempting Maven compilation...
echo   This will download dependencies (may take 5-10 minutes first time)
echo.

mvn clean compile -U
set MAVEN_EXIT_CODE=%errorlevel%

echo.
echo ========================================
if %MAVEN_EXIT_CODE% equ 0 (
    echo ✓ SUCCESS: Framework compiled successfully!
    echo.
    echo Next steps:
    echo   1. Run tests: mvn test
    echo   2. Run specific test: mvn test -Dtest=LoginTests#testPasswordMasking
    echo   3. Check reports in: target\reports\
) else (
    echo ✗ FAILED: Compilation failed
    echo.
    echo Troubleshooting options:
    echo   1. Check NETWORK_TROUBLESHOOTING.md for detailed solutions
    echo   2. Try using mobile hotspot or different network
    echo   3. Check if behind corporate firewall/proxy
    echo   4. Use offline version: copy pom-offline.xml to pom.xml
)

echo.
echo ========================================
pause