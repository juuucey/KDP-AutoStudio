package com.kdp.autostudio.dao;

import com.kdp.autostudio.model.Idea;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock implementation of IIdeaDAO for testing purposes.
 * Stores ideas in memory instead of a database.
 * This demonstrates the benefit of using interfaces - we can easily
 * swap implementations for testing without changing the service layer.
 */
public class MockIdeaDAO implements IIdeaDAO {
    private final ConcurrentHashMap<Integer, Idea> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public void insert(Idea idea) throws SQLException {
        if (idea == null) {
            throw new SQLException("Idea cannot be null");
        }
        int id = idGenerator.getAndIncrement();
        idea.setId(id);
        storage.put(id, idea);
    }

    @Override
    public List<Idea> findAll() throws SQLException {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Idea> findByStatus(String status) throws SQLException {
        List<Idea> result = new ArrayList<>();
        for (Idea idea : storage.values()) {
            if (status.equals(idea.getStatus())) {
                result.add(idea);
            }
        }
        return result;
    }

    @Override
    public Idea findById(int id) throws SQLException {
        return storage.get(id);
    }

    @Override
    public void updateStatus(int id, String status) throws SQLException {
        Idea idea = storage.get(id);
        if (idea != null) {
            idea.setStatus(status);
        } else {
            throw new SQLException("Idea with id " + id + " not found");
        }
    }

    /**
     * Clear all stored ideas (useful for testing).
     */
    public void clear() {
        storage.clear();
        idGenerator.set(1);
    }

    /**
     * Get the number of stored ideas.
     */
    public int size() {
        return storage.size();
    }
}

