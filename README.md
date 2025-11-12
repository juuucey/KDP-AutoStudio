# KDP AutoStudio (Local Edition)

AI-powered desktop research & design studio for Amazon KDP creators.

## Overview

KDP AutoStudio automates the entire Amazon KDP book creation pipeline — from market research and idea validation to interior and cover design — using AI reasoning (OpenAI) and AI design generation (Affinity AI).

## Features

- **🧠 AI Research & Idea Generation**: Automated competitor analysis and market insights
- **📊 Idea Backlog & Scoring**: Sort, filter, and approve profitable book ideas
- **🎨 AI-Driven Design**: Generate covers and interiors using Affinity AI
- **🧾 Metadata & SEO Assistant**: Auto-generate KDP-compliant metadata
- **🧰 Packaging & Validation**: Pre-flight checks and KDP-ready bundle export
- **📈 Analytics**: Track performance of published books

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Python 3.11+** with pip
- **OpenAI API Key** (for AI reasoning and content generation)
- **Affinity Designer/Publisher** (for design automation - optional for MVP)

## Installation

### 1. Clone or Download

```bash
cd "KDP AutoStudio"
```

### 2. Install Python Dependencies

```bash
cd python/worker
pip install -r requirements.txt
playwright install chromium
```

### 3. Build Java Application

```bash
mvn clean install
```

### 4. Configure Application

On first run, the application will create a configuration file at:
- **Windows**: `%USERPROFILE%\.kdp-autostudio\config.json`
- **macOS/Linux**: `~/.kdp-autostudio/config.json`

You'll need to add your OpenAI API key in the Settings menu.

## Running the Application

### Development Mode

```bash
mvn javafx:run
```

### Production Build

```bash
mvn clean package
java -jar target/autostudio-1.0.0-SNAPSHOT.jar
```

## Project Structure

```
KDP AutoStudio/
├── src/main/java/com/kdp/autostudio/
│   ├── KDPAutoStudioApp.java          # Main entry point
│   ├── config/                        # Configuration management
│   ├── database/                      # SQLite database manager
│   ├── dao/                           # Data access objects
│   ├── model/                         # Domain models
│   ├── service/                       # Business logic services
│   ├── ui/                            # JavaFX UI components
│   │   ├── MainWindow.java
│   │   └── panels/                    # UI panels
│   └── util/                          # Utilities
├── python/worker/                     # Python worker scripts
│   ├── main.py                        # Main worker entry point
│   ├── scraper/                       # Amazon scraper
│   ├── ai/                            # OpenAI integration
│   └── database/                      # Idea processing
├── pom.xml                            # Maven configuration
└── README.md
```

## Usage

### 1. Research & Ideas

1. Navigate to the **Research & Ideas** tab
2. Enter seed keywords (e.g., "gratitude journal", "kids handwriting workbook")
3. Click **Start Research**
4. The Python worker will scrape Amazon and analyze with OpenAI
5. Results appear in the **Idea Backlog** tab

### 2. Review Backlog

1. Go to **Idea Backlog** tab
2. Review scored ideas with AI explanations
3. Sort by score, demand, or competition
4. Approve promising ideas for production

### 3. Generate Book

1. Go to **Production** tab
2. Select an approved idea
3. Choose production options (interior, cover, metadata)
4. Click **Generate Book**
5. AI will generate content and design files

### 4. Package & Export

1. Go to **Package & Export** tab
2. Select a completed project
3. Click **Validate** to run pre-flight checks
4. Click **Export KDP Bundle** to create ZIP file

### 5. Track Performance (Optional)

1. Go to **Analytics** tab
2. Add published ASINs to track
3. View BSR trends and performance metrics

## Configuration

Edit `~/.kdp-autostudio/config.json` to customize:

- **Scoring Weights**: Adjust idea scoring formula weights
- **Paths**: Customize Projects, Templates, Assets directories
- **OpenAI Model**: Change AI model (default: gpt-4-turbo-preview)

## Scoring Model

Ideas are scored using:

```
overall_score = 0.35 * demand + 0.25 * (1 - competition) + 
               0.15 * margin + 0.15 * (1 - effort) + 0.10 * novelty
```

All values normalized [0-1]. Weights are configurable.

## Technology Stack

- **UI**: Java 21 + JavaFX
- **Database**: SQLite (embedded)
- **AI Reasoning**: OpenAI API (GPT-4)
- **Design AI**: Affinity AI (local)
- **Scraping**: Python + Playwright
- **Build**: Maven

## Development

### Running Tests

```bash
mvn test
```

### Code Style

Follow Java standard conventions. Use SLF4J for logging.

## Troubleshooting

### Python Worker Not Found

Ensure Python is in your PATH and the worker script path in config is correct.

### OpenAI API Errors

Check your API key in Settings. Ensure you have sufficient credits.

### Database Errors

Delete `~/.kdp-autostudio/autostudio.db` to reset the database.

## License

This is a local desktop application. All data stays on your machine.

## Support

For issues or questions, check the PRD.txt for detailed specifications.

## Roadmap

- [ ] Affinity AI macro integration
- [ ] Advanced pre-flight validation
- [ ] Series builder
- [ ] Multi-language support
- [ ] KDP uploader integration

---

**Note**: This application respects Amazon's robots.txt and terms of service. Use responsibly.

