package com.kdp.autostudio.config;

/**
 * Scoring weights for idea evaluation.
 * Used in the formula: overall_score = 0.35*demand + 0.25*(1-competition) + 0.15*margin + 0.15*(1-effort) + 0.10*novelty
 */
public class ScoringWeights {
    private double demand;
    private double competition;
    private double margin;
    private double effort;
    private double novelty;

    public ScoringWeights() {
        // Default weights from PRD
        this.demand = 0.35;
        this.competition = 0.25;
        this.margin = 0.15;
        this.effort = 0.15;
        this.novelty = 0.10;
    }

    public ScoringWeights(double demand, double competition, double margin, double effort, double novelty) {
        this.demand = demand;
        this.competition = competition;
        this.margin = margin;
        this.effort = effort;
        this.novelty = novelty;
    }

    // Getters and setters
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
}

