package com.kdp.autostudio.service;

import com.kdp.autostudio.config.ConfigManager;
import com.kdp.autostudio.dao.IdeaDAO;
import com.kdp.autostudio.model.Idea;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for managing research jobs and processing results.
 */
public class ResearchService {
    private static final Logger logger = LoggerFactory.getLogger(ResearchService.class);
    private final ConfigManager configManager;
    private final IdeaDAO ideaDAO;
    private final Gson gson;

    public ResearchService() {
        this.configManager = ConfigManager.getInstance();
        this.ideaDAO = new IdeaDAO();
        this.gson = new Gson();
    }

    /**
     * Start a research job for the given keywords.
     * Returns a CompletableFuture that completes when research is done.
     */
    public CompletableFuture<List<Idea>> startResearch(List<String> keywords) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Build Python command
                String pythonPath = "python3"; // or "python" on Windows
                String workerPath = configManager.getConfig().getPythonWorkerPath();
                String keywordsStr = String.join(",", keywords);
                String outputFile = System.getProperty("java.io.tmpdir") + File.separator + "research_output.json";

                // Execute Python worker
                ProcessBuilder pb = new ProcessBuilder(
                    pythonPath,
                    workerPath,
                    "--keywords", keywordsStr,
                    "--openai-key", configManager.getOpenAIApiKey(),
                    "--output", outputFile
                );

                Process process = pb.start();

                // Read output
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("Python worker: {}", line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("Python worker failed with exit code: " + exitCode);
                }

                // Parse results
                List<Idea> ideas = parseResults(outputFile);

                // Save to database
                for (Idea idea : ideas) {
                    idea.calculateScore(configManager.getScoringWeights());
                    ideaDAO.insert(idea);
                }

                return ideas;

            } catch (Exception e) {
                logger.error("Research job failed", e);
                throw new RuntimeException("Research failed", e);
            }
        });
    }

    private List<Idea> parseResults(String outputFile) throws IOException {
        List<Idea> ideas = new ArrayList<>();

        Path path = Paths.get(outputFile);
        if (!Files.exists(path)) {
            logger.warn("Research output file not found: {}", outputFile);
            return ideas;
        }

        String json = Files.readString(path);
        JsonArray jsonArray = gson.fromJson(json, JsonArray.class);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            Idea idea = new Idea();
            idea.setKeyword(obj.get("keyword").getAsString());
            idea.setTitle(obj.has("title") ? obj.get("title").getAsString() : null);
            idea.setSubtitle(obj.has("subtitle") ? obj.get("subtitle").getAsString() : null);
            idea.setDemand(obj.get("demand").getAsDouble());
            idea.setCompetition(obj.get("competition").getAsDouble());
            idea.setMargin(obj.get("margin").getAsDouble());
            idea.setEffort(obj.get("effort").getAsDouble());
            idea.setNovelty(obj.get("novelty").getAsDouble());
            idea.setScore(obj.get("score").getAsDouble());
            idea.setRisk(obj.has("risk") ? obj.get("risk").getAsString() : null);
            idea.setProfitability(obj.has("profitability") ? obj.get("profitability").getAsString() : null);
            idea.setAiExplanation(obj.has("ai_explanation") ? obj.get("ai_explanation").getAsString() : null);
            idea.setStatus(obj.has("status") ? obj.get("status").getAsString() : "pending");

            ideas.add(idea);
        }

        return ideas;
    }
}

