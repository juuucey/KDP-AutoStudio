package com.kdp.autostudio.dao;

import com.kdp.autostudio.database.DatabaseManager;
import com.kdp.autostudio.model.Idea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Idea entities.
 */
public class IdeaDAO {
    private static final Logger logger = LoggerFactory.getLogger(IdeaDAO.class);

    public void insert(Idea idea) throws SQLException {
        String sql = """
            INSERT INTO ideas (keyword, title, subtitle, demand, competition, margin, effort, novelty, 
                             score, risk, profitability, ai_explanation, status, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, idea.getKeyword());
            stmt.setString(2, idea.getTitle());
            stmt.setString(3, idea.getSubtitle());
            stmt.setDouble(4, idea.getDemand());
            stmt.setDouble(5, idea.getCompetition());
            stmt.setDouble(6, idea.getMargin());
            stmt.setDouble(7, idea.getEffort());
            stmt.setDouble(8, idea.getNovelty());
            stmt.setDouble(9, idea.getScore());
            stmt.setString(10, idea.getRisk());
            stmt.setString(11, idea.getProfitability());
            stmt.setString(12, idea.getAiExplanation());
            stmt.setString(13, idea.getStatus());
            stmt.setTimestamp(14, Timestamp.valueOf(idea.getCreatedAt()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idea.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Idea> findAll() throws SQLException {
        String sql = "SELECT * FROM ideas ORDER BY score DESC";
        List<Idea> ideas = new ArrayList<>();

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ideas.add(mapRowToIdea(rs));
            }
        }

        return ideas;
    }

    public List<Idea> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM ideas WHERE status = ? ORDER BY score DESC";
        List<Idea> ideas = new ArrayList<>();

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ideas.add(mapRowToIdea(rs));
                }
            }
        }

        return ideas;
    }

    public Idea findById(int id) throws SQLException {
        String sql = "SELECT * FROM ideas WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToIdea(rs);
                }
            }
        }

        return null;
    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE ideas SET status = ?, approved_at = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            if ("approved".equals(status)) {
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }
            stmt.setInt(3, id);

            stmt.executeUpdate();
        }
    }

    private Idea mapRowToIdea(ResultSet rs) throws SQLException {
        Idea idea = new Idea();
        idea.setId(rs.getInt("id"));
        idea.setKeyword(rs.getString("keyword"));
        idea.setTitle(rs.getString("title"));
        idea.setSubtitle(rs.getString("subtitle"));
        idea.setDemand(rs.getDouble("demand"));
        idea.setCompetition(rs.getDouble("competition"));
        idea.setMargin(rs.getDouble("margin"));
        idea.setEffort(rs.getDouble("effort"));
        idea.setNovelty(rs.getDouble("novelty"));
        idea.setScore(rs.getDouble("score"));
        idea.setRisk(rs.getString("risk"));
        idea.setProfitability(rs.getString("profitability"));
        idea.setAiExplanation(rs.getString("ai_explanation"));
        idea.setStatus(rs.getString("status"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            idea.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp approvedAt = rs.getTimestamp("approved_at");
        if (approvedAt != null) {
            idea.setApprovedAt(approvedAt.toLocalDateTime());
        }

        return idea;
    }
}

