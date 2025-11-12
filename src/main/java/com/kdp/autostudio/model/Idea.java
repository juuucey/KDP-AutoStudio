package com.kdp.autostudio.model;

import java.time.LocalDateTime;

/**
 * Represents a book idea with scoring attributes.
 */
public class Idea {
    private Integer id;
    private String keyword;
    private String title;
    private String subtitle;
    private double demand;
    private double competition;
    private double margin;
    private double effort;
    private double novelty;
    private double score;
    private String risk;
    private String profitability;
    private String aiExplanation;
    private String status; // pending, approved, rejected, in_production
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    // Constructors
    public Idea() {
        this.status = "pending";
        this.createdAt = LocalDateTime.now();
    }

    public Idea(String keyword) {
        this();
        this.keyword = keyword;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    public double getCompetition() {
        return competition;
    }

    public void setCompetition(double competition) {
        this.competition = competition;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getEffort() {
        return effort;
    }

    public void setEffort(double effort) {
        this.effort = effort;
    }

    public double getNovelty() {
        return novelty;
    }

    public void setNovelty(double novelty) {
        this.novelty = novelty;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getProfitability() {
        return profitability;
    }

    public void setProfitability(String profitability) {
        this.profitability = profitability;
    }

    public String getAiExplanation() {
        return aiExplanation;
    }

    public void setAiExplanation(String aiExplanation) {
        this.aiExplanation = aiExplanation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    /**
     * Calculates the overall score based on configured weights.
     */
    public void calculateScore(com.kdp.autostudio.config.ScoringWeights weights) {
        this.score = weights.getDemand() * demand +
                    weights.getCompetition() * (1 - competition) +
                    weights.getMargin() * margin +
                    weights.getEffort() * (1 - effort) +
                    weights.getNovelty() * novelty;
    }
}

