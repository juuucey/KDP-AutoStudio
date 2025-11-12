@echo off
REM KDP AutoStudio Setup Script for Windows

echo ========================================
echo KDP AutoStudio Setup
echo ========================================
echo.

REM Check Java
echo Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java 21 or higher is required but not found in PATH.
    echo Please install Java 21 from https://adoptium.net/
    pause
    exit /b 1
)
echo Java found.
echo.

REM Check Maven
echo Checking Maven installation...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is required but not found in PATH.
    echo Please install Maven from https://maven.apache.org/
    pause
    exit /b 1
)
echo Maven found.
echo.

REM Check Python
echo Checking Python installation...
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Python 3.11+ is required but not found in PATH.
    echo Please install Python from https://www.python.org/
    pause
    exit /b 1
)
echo Python found.
echo.

REM Install Python dependencies
echo Installing Python dependencies...
cd python\worker
pip install -r requirements.txt
if %errorlevel% neq 0 (
    echo ERROR: Failed to install Python dependencies.
    pause
    exit /b 1
)
echo Python dependencies installed.
echo.

REM Install Playwright browsers
echo Installing Playwright browsers...
playwright install chromium
if %errorlevel% neq 0 (
    echo WARNING: Failed to install Playwright browsers. You may need to run this manually.
)
echo.

REM Build Java application
echo Building Java application...
cd ..\..
mvn clean install
if %errorlevel% neq 0 (
    echo ERROR: Maven build failed.
    pause
    exit /b 1
)
echo.
echo ========================================
echo Setup complete!
echo ========================================
echo.
echo Next steps:
echo 1. Run the application: mvn javafx:run
echo 2. Configure your OpenAI API key in Settings
echo 3. Start researching book ideas!
echo.
pause

