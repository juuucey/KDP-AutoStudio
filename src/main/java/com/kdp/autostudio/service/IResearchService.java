package com.kdp.autostudio.service;

import com.kdp.autostudio.model.Idea;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for managing research operations.
 * Provides abstraction for research functionality, allowing for
 * different implementations (local Python worker, remote API, mock for testing, etc.)
 */
public interface IResearchService {
    /**
     * Start a research job for the given keywords.
     * This method runs asynchronously and returns a CompletableFuture that
     * completes when the research is finished.
     *
     * @param keywords List of seed keywords to research (e.g., "gratitude journal", "kids activity book")
     * @return CompletableFuture that will contain the list of generated ideas when complete
     */
    CompletableFuture<List<Idea>> startResearch(List<String> keywords);
}

