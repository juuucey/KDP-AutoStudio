package com.kdp.autostudio.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Production panel for generating book covers and interiors using AI.
 */
public class ProductionPanel {
    private ComboBox<String> ideaCombo;
    private TextArea metadataArea;
    private ProgressBar progressBar;
    private TextArea logArea;

    public ProductionPanel() {
    }

    public BorderPane getContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section: Idea selection
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Book Production Wizard");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox ideaBox = new HBox(10);
        ideaBox.getChildren().add(new Label("Select Approved Idea:"));
        ideaCombo = new ComboBox<>();
        ideaCombo.setPromptText("Select an approved idea from backlog");
        ideaCombo.setPrefWidth(400);
        ideaBox.getChildren().add(ideaCombo);

        topSection.getChildren().addAll(titleLabel, ideaBox);

        // Center: Production options
        VBox centerSection = new VBox(15);
        centerSection.setPadding(new Insets(10));

        Label optionsLabel = new Label("Production Options:");
        CheckBox generateInterior = new CheckBox("Generate Interior Layout");
        generateInterior.setSelected(true);
        CheckBox generateCover = new CheckBox("Generate Cover Design");
        generateCover.setSelected(true);
        CheckBox generateMetadata = new CheckBox("Generate Metadata & SEO");
        generateMetadata.setSelected(true);

        Label metadataLabel = new Label("Metadata Preview:");
        metadataArea = new TextArea();
        metadataArea.setEditable(false);
        metadataArea.setPrefRowCount(8);
        metadataArea.setText("Metadata will be generated here...");

        Button generateButton = new Button("Generate Book");
        generateButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        generateButton.setOnAction(e -> startProduction());

        centerSection.getChildren().addAll(optionsLabel, generateInterior, generateCover, generateMetadata,
                metadataLabel, metadataArea, generateButton);

        // Bottom: Progress and logs
        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(10));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);

        Label logLabel = new Label("Production Log:");
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(10);
        logArea.setText("Ready to generate. Select an idea and click 'Generate Book'.");

        bottomSection.getChildren().addAll(progressBar, logLabel, logArea);

        root.setTop(topSection);
        root.setCenter(centerSection);
        root.setBottom(bottomSection);

        return root;
    }

    private void startProduction() {
        String selectedIdea = ideaCombo.getValue();
        if (selectedIdea == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an approved idea first.");
            alert.showAndWait();
            return;
        }

        // TODO: Implement production workflow
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        logArea.appendText("\n\nStarting production for: " + selectedIdea);
        logArea.appendText("\n[This will trigger OpenAI for content and Affinity AI for design]");

        // Simulate async operation
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> {
                    logArea.appendText("\nProduction completed! Check Package & Export tab.");
                    progressBar.setProgress(1.0);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

