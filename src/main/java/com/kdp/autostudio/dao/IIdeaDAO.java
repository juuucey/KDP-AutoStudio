package com.kdp.autostudio.dao;

import com.kdp.autostudio.model.Idea;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Idea entities.
 * Provides abstraction for data access operations, allowing for
 * different implementations (SQLite, PostgreSQL, in-memory, etc.)
 * and easier testing with mock implementations.
 */
public interface IIdeaDAO {
    /**
     * Insert a new idea into the database.
     *
     * @param idea The idea to insert
     * @throws SQLException if database operation fails
     */
    void insert(Idea idea) throws SQLException;

    /**
     * Retrieve all ideas from the database, ordered by score descending.
     *
     * @return List of all ideas
     * @throws SQLException if database operation fails
     */
    List<Idea> findAll() throws SQLException;

    /**
     * Find all ideas with a specific status.
     *
     * @param status The status to filter by (e.g., "pending", "approved", "rejected")
     * @return List of ideas with the specified status
     * @throws SQLException if database operation fails
     */
    List<Idea> findByStatus(String status) throws SQLException;

    /**
     * Find an idea by its ID.
     *
     * @param id The idea ID
     * @return The idea if found, null otherwise
     * @throws SQLException if database operation fails
     */
    Idea findById(int id) throws SQLException;

    /**
     * Update the status of an idea.
     *
     * @param id The idea ID
     * @param status The new status
     * @throws SQLException if database operation fails
     */
    void updateStatus(int id, String status) throws SQLException;
}

