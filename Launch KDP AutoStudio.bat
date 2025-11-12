@echo off
REM KDP AutoStudio Launcher
REM One-click launcher for KDP AutoStudio application

title KDP AutoStudio Launcher

REM Get the directory where this script is located
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

echo ========================================
echo KDP AutoStudio
echo ========================================
echo.

REM Check if JAR exists - this is the preferred method
if exist "target\autostudio-1.0.0-SNAPSHOT.jar" (
    echo Launching from JAR file...
    echo.
    REM Try with JavaFX module path if lib directory exists
    if exist "target\lib" (
        java --module-path "target\lib" --add-modules javafx.controls,javafx.fxml -jar "target\autostudio-1.0.0-SNAPSHOT.jar"
    ) else (
        java -jar "target\autostudio-1.0.0-SNAPSHOT.jar"
    )
    if %errorlevel% equ 0 (
        exit /b 0
    )
    echo.
    echo JAR launch failed. Error code: %errorlevel%
    echo.
    echo Checking if Java is installed...
    java -version >nul 2>&1
    if %errorlevel% neq 0 (
        echo ERROR: Java is not installed or not in PATH.
        echo Please install Java 21 or higher.
        echo.
        pause
        exit /b 1
    )
    echo Java is installed. The JAR file may be corrupted.
    echo Please rebuild using: build-and-launch.bat
    echo.
    pause
    exit /b 1
)

REM JAR doesn't exist, try Maven as fallback
echo JAR file not found. Checking for Maven...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Neither JAR file nor Maven found.
    echo.
    echo Please either:
    echo 1. Run build-and-launch.bat to build the JAR file, OR
    echo 2. Install Maven and add it to PATH
    echo.
    pause
    exit /b 1
)

echo Maven found. Launching with Maven...
echo.
mvn javafx:run

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo Launch failed!
    echo ========================================
    echo.
    echo Troubleshooting:
    echo 1. Ensure Java 21+ is installed
    echo 2. Ensure Maven is installed and in PATH
    echo 3. Try running: mvn clean install
    echo.
    pause
)

