# Quick Start Guide

## Prerequisites Checklist

- [ ] Java 21 or higher installed
- [ ] Maven 3.6+ installed
- [ ] Python 3.11+ installed
- [ ] OpenAI API key (get one at https://platform.openai.com/api-keys)

## Installation Steps

### Windows

1. Open PowerShell or Command Prompt in the project directory
2. Run: `.\setup.bat`
3. Follow the prompts

### macOS/Linux

1. Open Terminal in the project directory
2. Run: `chmod +x setup.sh && ./setup.sh`
3. Follow the prompts

### Manual Setup

If the setup script doesn't work, follow these steps:

1. **Install Python dependencies:**
   ```bash
   cd python/worker
   pip install -r requirements.txt
   playwright install chromium
   ```

2. **Build Java application:**
   ```bash
   mvn clean install
   ```

## Running the Application

```bash
mvn javafx:run
```

Or if you've built the JAR:

```bash
java -jar target/autostudio-1.0.0-SNAPSHOT.jar
```

## First-Time Configuration

1. When the app starts, go to **File > Settings**
2. Enter your OpenAI API key
3. Configure scoring weights if desired (defaults are fine)
4. Click **Save**

## Your First Research Job

1. Go to the **Research & Ideas** tab
2. Enter keywords like: `gratitude journal, kids activity book`
3. Click **Start Research**
4. Wait for the Python worker to scrape and analyze (2-5 minutes)
5. Check the **Idea Backlog** tab for scored ideas
6. Approve promising ideas
7. Generate books in the **Production** tab

## Troubleshooting

### "Python worker not found"
- Check that `python/worker/main.py` exists
- Verify Python is in your PATH
- Check the Python worker path in Settings

### "OpenAI API error"
- Verify your API key is correct
- Check you have API credits
- Ensure internet connection

### "Database error"
- Delete `~/.kdp-autostudio/autostudio.db` to reset
- Check file permissions

### JavaFX not loading
- Ensure Java 21+ is installed
- Try: `mvn clean install` then run again

## Project Structure

Your projects will be saved to:
- **Windows**: `%USERPROFILE%\KDP_AutoStudio\Projects\`
- **macOS/Linux**: `~/KDP_AutoStudio/Projects/`

Configuration and database:
- **Windows**: `%USERPROFILE%\.kdp-autostudio\`
- **macOS/Linux**: `~/.kdp-autostudio/`

## Next Steps

- Read the full [README.md](README.md) for detailed documentation
- Review [PRD.txt](PRD.txt) for product specifications
- Start researching profitable book niches!

