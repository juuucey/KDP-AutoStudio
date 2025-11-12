@echo off
REM KDP AutoStudio JavaFX Launcher
REM Uses Maven JavaFX plugin which handles all JavaFX setup automatically

title KDP AutoStudio

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

echo ========================================
echo KDP AutoStudio
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH.
    echo.
    echo The JavaFX launcher requires Maven.
    echo Please install Maven and add it to PATH.
    echo.
    pause
    exit /b 1
)

echo Launching with JavaFX Maven plugin...
echo (This automatically handles all JavaFX dependencies)
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
    exit /b 1
)

