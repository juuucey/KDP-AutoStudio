package com.kdp.autostudio.config;

/**
 * Application configuration model.
 */
public class AppConfig {
    private String openAIApiKey;
    private ScoringWeights scoringWeights;
    private String projectsPath;
    private String templatesPath;
    private String assetsPath;
    private String pythonWorkerPath;
    private String openAIModel = "gpt-4-turbo-preview";

    // Getters and setters
    public String getOpenAIApiKey() {
        return openAIApiKey;
    }

    public void setOpenAIApiKey(String openAIApiKey) {
        this.openAIApiKey = openAIApiKey;
    }

    public ScoringWeights getScoringWeights() {
        return scoringWeights;
    }

    public void setScoringWeights(ScoringWeights scoringWeights) {
        this.scoringWeights = scoringWeights;
    }

    public String getProjectsPath() {
        return projectsPath;
    }

    public void setProjectsPath(String projectsPath) {
        this.projectsPath = projectsPath;
    }

    public String getTemplatesPath() {
        return templatesPath;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(String assetsPath) {
        this.assetsPath = assetsPath;
    }

    public String getPythonWorkerPath() {
        return pythonWorkerPath;
    }

    public void setPythonWorkerPath(String pythonWorkerPath) {
        this.pythonWorkerPath = pythonWorkerPath;
    }

    public String getOpenAIModel() {
        return openAIModel;
    }

    public void setOpenAIModel(String openAIModel) {
        this.openAIModel = openAIModel;
    }
}

