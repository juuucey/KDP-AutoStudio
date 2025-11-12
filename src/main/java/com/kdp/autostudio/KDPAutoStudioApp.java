package com.kdp.autostudio;

import com.kdp.autostudio.ui.MainWindow;
import com.kdp.autostudio.config.ConfigManager;
import com.kdp.autostudio.database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for KDP AutoStudio application.
 * AI-powered desktop research & design studio for Amazon KDP creators.
 */
public class KDPAutoStudioApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(KDPAutoStudioApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize configuration
            ConfigManager configManager = ConfigManager.getInstance();
            logger.info("Configuration loaded");

            // Initialize file system
            com.kdp.autostudio.util.FileSystemUtil.initializeDirectories();
            logger.info("File system initialized");

            // Initialize database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            dbManager.initializeDatabase();
            logger.info("Database initialized");

            // Launch main window
            MainWindow mainWindow = new MainWindow(primaryStage);
            mainWindow.show();
            logger.info("Application started successfully");

        } catch (Exception e) {
            logger.error("Failed to start application", e);
            throw new RuntimeException("Application initialization failed", e);
        }
    }

    @Override
    public void stop() {
        // Cleanup resources
        DatabaseManager.getInstance().close();
        logger.info("Application stopped");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

