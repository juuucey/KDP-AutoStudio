package com.kdp.autostudio.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages application configuration including API keys, scoring weights, and paths.
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".kdp-autostudio";
    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.json";

    private AppConfig config;
    private final Gson gson;

    private ConfigManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        loadConfig();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            Path configPath = Paths.get(CONFIG_FILE);
            if (Files.exists(configPath)) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    config = gson.fromJson(reader, AppConfig.class);
                    logger.info("Configuration loaded from {}", CONFIG_FILE);
                }
            } else {
                // Create default configuration
                config = createDefaultConfig();
                saveConfig();
                logger.info("Default configuration created at {}", CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
            config = createDefaultConfig();
        }
    }

    private AppConfig createDefaultConfig() {
        AppConfig defaultConfig = new AppConfig();
        
        // Default scoring weights
        defaultConfig.setScoringWeights(new ScoringWeights(0.35, 0.25, 0.15, 0.15, 0.10));
        
        // Default paths
        String userHome = System.getProperty("user.home");
        defaultConfig.setProjectsPath(userHome + File.separator + "KDP_AutoStudio" + File.separator + "Projects");
        defaultConfig.setTemplatesPath(userHome + File.separator + "KDP_AutoStudio" + File.separator + "Templates");
        defaultConfig.setAssetsPath(userHome + File.separator + "KDP_AutoStudio" + File.separator + "Assets");
        
        // Python worker path (relative to project root)
        String projectRoot = System.getProperty("user.dir");
        defaultConfig.setPythonWorkerPath(projectRoot + File.separator + "python" + File.separator + "worker" + File.separator + "main.py");
        
        return defaultConfig;
    }

    public void saveConfig() {
        try {
            // Ensure config directory exists
            Path configDir = Paths.get(CONFIG_DIR);
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                gson.toJson(config, writer);
                logger.info("Configuration saved to {}", CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Failed to save configuration", e);
        }
    }

    public AppConfig getConfig() {
        return config;
    }

    public void setOpenAIApiKey(String apiKey) {
        config.setOpenAIApiKey(apiKey);
        saveConfig();
    }

    public String getOpenAIApiKey() {
        return config.getOpenAIApiKey();
    }

    public ScoringWeights getScoringWeights() {
        return config.getScoringWeights();
    }

    public void setScoringWeights(ScoringWeights weights) {
        config.setScoringWeights(weights);
        saveConfig();
    }

    public String getProjectsPath() {
        return config.getProjectsPath();
    }

    public String getTemplatesPath() {
        return config.getTemplatesPath();
    }

    public String getAssetsPath() {
        return config.getAssetsPath();
    }
}

