# KDP AutoStudio Launchers

Multiple launcher options are available for your convenience:

## 🚀 Recommended Launchers

### 1. **launch-javafx.bat** ⭐ (Recommended - Most Reliable)
- **Double-click to run**
- Uses Maven JavaFX plugin (handles all JavaFX setup automatically)
- Most reliable way to launch the application
- Automatically handles JavaFX dependencies
- Shows console output (helpful for debugging)

**Usage:** Simply double-click `launch-javafx.bat`

### 2. **Launch KDP AutoStudio.bat**
- **Double-click to run**
- Tries to run from JAR file first, falls back to Maven
- Shows console output
- Works on all Windows versions

**Usage:** Double-click `Launch KDP AutoStudio.bat`

### 3. **Launch KDP AutoStudio.vbs** (Silent/No Console)
- **Double-click to run**
- Runs without showing a console window
- Cleaner experience for end users
- Still shows the app window

**Usage:** Double-click `Launch KDP AutoStudio.vbs`

### 4. **Launch KDP AutoStudio.ps1** (PowerShell)
- More advanced error handling
- Colored output
- Requires PowerShell (Windows 10+)

**Usage:** Right-click → "Run with PowerShell" or double-click (if associated)

## 📋 How They Work

- **launch-javafx.bat**: Uses `mvn javafx:run` which automatically handles all JavaFX dependencies and module setup
- **Launch KDP AutoStudio.bat**: Tries JAR first, falls back to Maven if needed
- **Other launchers**: Wrapper scripts for different preferences

## 🎯 Quick Start

1. **Recommended**: Double-click `launch-javafx.bat` - it just works!
2. **Alternative**: Use `Launch KDP AutoStudio.bat` if you prefer

## 💡 Tips

- **Desktop Shortcut**: Right-click `Launch KDP AutoStudio.bat` → "Create shortcut" → Move to Desktop
- **Pin to Taskbar**: Create shortcut, then right-click → "Pin to taskbar"
- **Custom Icon**: You can change the shortcut icon to a custom image

## ⚠️ Troubleshooting

If launchers don't work:

1. **Use launch-javafx.bat**: This is the most reliable launcher
2. **Check Java**: Run `java -version` (should be Java 21+)
3. **Check Maven**: Run `mvn -version` (required for launch-javafx.bat)
4. **Check paths**: Ensure you're running from the project root directory
5. **Rebuild if needed**: Run `mvn clean install` if you've made code changes

## 🔧 Requirements

- Java 21 or higher
- Maven 3.6+ (for fallback mode)
- Windows 7+ (for .bat files)
- Windows 10+ (for PowerShell script)

