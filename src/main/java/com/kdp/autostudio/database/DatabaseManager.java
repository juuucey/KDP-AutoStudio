package com.kdp.autostudio.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages SQLite database connection and initialization.
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private static final String DB_DIR = System.getProperty("user.home") + File.separator + ".kdp-autostudio";
    private static final String DB_FILE = DB_DIR + File.separator + "autostudio.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    private Connection connection;

    private DatabaseManager() {
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initializeDatabase() {
        try {
            // Ensure database directory exists
            File dbDir = new File(DB_DIR);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            connection = DriverManager.getConnection(DB_URL);
            logger.info("Connected to database: {}", DB_FILE);

            createTables();
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Ideas table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS ideas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    keyword TEXT NOT NULL,
                    title TEXT,
                    subtitle TEXT,
                    demand REAL DEFAULT 0.0,
                    competition REAL DEFAULT 0.0,
                    margin REAL DEFAULT 0.0,
                    effort REAL DEFAULT 0.0,
                    novelty REAL DEFAULT 0.0,
                    score REAL DEFAULT 0.0,
                    risk TEXT,
                    profitability TEXT,
                    ai_explanation TEXT,
                    status TEXT DEFAULT 'pending',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    approved_at TIMESTAMP
                )
            """);

            // Competitors table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS competitors (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    idea_id INTEGER,
                    asin TEXT,
                    title TEXT,
                    subtitle TEXT,
                    price REAL,
                    bsr INTEGER,
                    category TEXT,
                    review_count INTEGER,
                    rating REAL,
                    scraped_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (idea_id) REFERENCES ideas(id)
                )
            """);

            // Projects table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS projects (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    idea_id INTEGER,
                    title TEXT NOT NULL,
                    subtitle TEXT,
                    status TEXT DEFAULT 'draft',
                    interior_path TEXT,
                    cover_path TEXT,
                    metadata_path TEXT,
                    bundle_path TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    completed_at TIMESTAMP,
                    FOREIGN KEY (idea_id) REFERENCES ideas(id)
                )
            """);

            // Analytics table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS analytics (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    project_id INTEGER,
                    asin TEXT,
                    bsr INTEGER,
                    price REAL,
                    review_count INTEGER,
                    rating REAL,
                    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (project_id) REFERENCES projects(id)
                )
            """);

            logger.info("Database tables created successfully");
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Database not initialized. Call initializeDatabase() first.");
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}

