# KDP AutoStudio PowerShell Launcher
# One-click launcher for KDP AutoStudio application

$ErrorActionPreference = "Stop"

# Get script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "KDP AutoStudio" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if JAR exists
$jarPath = Join-Path $scriptDir "target\autostudio-1.0.0-SNAPSHOT.jar"
if (Test-Path $jarPath) {
    Write-Host "Launching from JAR file..." -ForegroundColor Green
    Write-Host ""
    try {
        & java -jar $jarPath
        exit 0
    } catch {
        Write-Host "JAR launch failed, trying Maven..." -ForegroundColor Yellow
        Write-Host ""
    }
}

# Fallback to Maven
Write-Host "Checking Maven installation..." -ForegroundColor Yellow
try {
    $mvnVersion = & mvn -version 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "Maven not found"
    }
} catch {
    Write-Host "ERROR: Maven is not installed or not in PATH." -ForegroundColor Red
    Write-Host "Please install Maven or build the JAR file first." -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Launching with Maven..." -ForegroundColor Green
Write-Host ""
try {
    & mvn javafx:run
    if ($LASTEXITCODE -ne 0) {
        throw "Maven launch failed"
    }
} catch {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "Launch failed!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Yellow
    Write-Host "1. Ensure Java 21+ is installed"
    Write-Host "2. Ensure Maven is installed and in PATH"
    Write-Host "3. Try running: mvn clean install"
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

