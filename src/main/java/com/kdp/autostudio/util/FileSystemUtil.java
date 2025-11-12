package com.kdp.autostudio.util;

import com.kdp.autostudio.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for managing application directories and file structure.
 */
public class FileSystemUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemUtil.class);
    private static final ConfigManager configManager = ConfigManager.getInstance();

    /**
     * Initialize application directory structure.
     */
    public static void initializeDirectories() {
        try {
            createDirectoryIfNotExists(configManager.getProjectsPath());
            createDirectoryIfNotExists(configManager.getTemplatesPath());
            createDirectoryIfNotExists(configManager.getAssetsPath());
            logger.info("Application directories initialized");
        } catch (Exception e) {
            logger.error("Failed to initialize directories", e);
        }
    }

    /**
     * Create a project directory for a book.
     */
    public static String createProjectDirectory(String bookTitle) {
        String sanitized = sanitizeFileName(bookTitle);
        String projectPath = configManager.getProjectsPath() + File.separator + sanitized;

        createDirectoryIfNotExists(projectPath);
        createDirectoryIfNotExists(projectPath + File.separator + "interior");
        createDirectoryIfNotExists(projectPath + File.separator + "cover");
        createDirectoryIfNotExists(projectPath + File.separator + "exports");

        return projectPath;
    }

    private static void createDirectoryIfNotExists(String path) {
        try {
            Path dir = Paths.get(path);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
                logger.debug("Created directory: {}", path);
            }
        } catch (Exception e) {
            logger.error("Failed to create directory: {}", path, e);
            throw new RuntimeException("Directory creation failed", e);
        }
    }

    private static String sanitizeFileName(String name) {
        // Remove invalid characters for file names
        return name.replaceAll("[<>:\"/\\|?*]", "_")
                   .replaceAll("\\s+", "_")
                   .trim();
    }
}

