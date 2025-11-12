#!/bin/bash
# KDP AutoStudio Setup Script for macOS/Linux

echo "========================================"
echo "KDP AutoStudio Setup"
echo "========================================"
echo ""

# Check Java
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java 21 or higher is required but not found in PATH."
    echo "Please install Java 21 from https://adoptium.net/"
    exit 1
fi
java -version
echo ""

# Check Maven
echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is required but not found in PATH."
    echo "Please install Maven from https://maven.apache.org/"
    exit 1
fi
mvn -version
echo ""

# Check Python
echo "Checking Python installation..."
if ! command -v python3 &> /dev/null; then
    echo "ERROR: Python 3.11+ is required but not found in PATH."
    echo "Please install Python from https://www.python.org/"
    exit 1
fi
python3 --version
echo ""

# Install Python dependencies
echo "Installing Python dependencies..."
cd python/worker
pip3 install -r requirements.txt
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to install Python dependencies."
    exit 1
fi
echo "Python dependencies installed."
echo ""

# Install Playwright browsers
echo "Installing Playwright browsers..."
playwright install chromium
if [ $? -ne 0 ]; then
    echo "WARNING: Failed to install Playwright browsers. You may need to run this manually."
fi
echo ""

# Build Java application
echo "Building Java application..."
cd ../..
mvn clean install
if [ $? -ne 0 ]; then
    echo "ERROR: Maven build failed."
    exit 1
fi
echo ""
echo "========================================"
echo "Setup complete!"
echo "========================================"
echo ""
echo "Next steps:"
echo "1. Run the application: mvn javafx:run"
echo "2. Configure your OpenAI API key in Settings"
echo "3. Start researching book ideas!"
echo ""

