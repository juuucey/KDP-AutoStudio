package com.kdp.autostudio.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Research panel for entering seed keywords and triggering research jobs.
 */
public class ResearchPanel {
    private TextField keywordInput;
    private TextArea progressArea;
    private Button startResearchButton;
    private ProgressBar progressBar;

    public ResearchPanel() {
    }

    public BorderPane getContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section: Input
        VBox inputSection = new VBox(10);
        inputSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Market Research & Idea Generation");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Enter seed keywords to research profitable book niches:");
        keywordInput = new TextField();
        keywordInput.setPromptText("e.g., gratitude journal, kids handwriting workbook, sudoku puzzles");

        HBox buttonBox = new HBox(10);
        startResearchButton = new Button("Start Research");
        startResearchButton.setOnAction(e -> startResearch());
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> keywordInput.clear());
        buttonBox.getChildren().addAll(startResearchButton, clearButton);

        inputSection.getChildren().addAll(titleLabel, instructionLabel, keywordInput, buttonBox);

        // Center section: Progress
        VBox progressSection = new VBox(10);
        progressSection.setPadding(new Insets(10));

        Label progressLabel = new Label("Research Progress:");
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);

        progressArea = new TextArea();
        progressArea.setEditable(false);
        progressArea.setPrefRowCount(15);
        progressArea.setText("Ready to start research. Enter keywords above and click 'Start Research'.");

        progressSection.getChildren().addAll(progressLabel, progressBar, progressArea);

        root.setTop(inputSection);
        root.setCenter(progressSection);

        return root;
    }

    private void startResearch() {
        String keywords = keywordInput.getText().trim();
        if (keywords.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Keywords");
            alert.setContentText("Please enter at least one keyword to research.");
            alert.showAndWait();
            return;
        }

        // TODO: Implement research job execution
        startResearchButton.setDisable(true);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progressArea.appendText("\n\nStarting research for: " + keywords);
        progressArea.appendText("\n[This will trigger Python worker to scrape Amazon and analyze with OpenAI]");

        // Simulate async operation
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate work
                javafx.application.Platform.runLater(() -> {
                    progressArea.appendText("\nResearch completed! Check the Idea Backlog tab for results.");
                    progressBar.setProgress(1.0);
                    startResearchButton.setDisable(false);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

