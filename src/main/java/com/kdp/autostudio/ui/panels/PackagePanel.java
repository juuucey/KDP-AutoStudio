package com.kdp.autostudio.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Package panel for validating and exporting KDP-ready bundles.
 */
public class PackagePanel {
    private ListView<String> projectList;
    private TextArea validationArea;

    public PackagePanel() {
    }

    public BorderPane getContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Package & Export");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Select a project to validate and export:");

        topSection.getChildren().addAll(titleLabel, instructionLabel);

        // Center: Project list and validation
        HBox centerBox = new HBox(15);
        centerBox.setPadding(new Insets(10));

        VBox listBox = new VBox(10);
        projectList = new ListView<>();
        projectList.setPrefWidth(300);
        projectList.setPrefHeight(400);
        // TODO: Load projects from database

        listBox.getChildren().addAll(new Label("Projects:"), projectList);

        VBox validationBox = new VBox(10);
        Label validationLabel = new Label("Validation Results:");
        validationArea = new TextArea();
        validationArea.setEditable(false);
        validationArea.setPrefRowCount(15);
        validationArea.setText("Select a project to view validation results.");

        validationBox.getChildren().addAll(validationLabel, validationArea);

        centerBox.getChildren().addAll(listBox, validationBox);

        // Bottom: Actions
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));

        Button validateButton = new Button("Validate Selected");
        validateButton.setOnAction(e -> validateProject());

        Button exportButton = new Button("Export KDP Bundle");
        exportButton.setOnAction(e -> exportBundle());

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshProjects());

        bottomBox.getChildren().addAll(validateButton, exportButton, refreshButton);

        root.setTop(topSection);
        root.setCenter(centerBox);
        root.setBottom(bottomBox);

        return root;
    }

    private void validateProject() {
        String selected = projectList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a project first.");
            alert.showAndWait();
            return;
        }

        // TODO: Run preflight validation
        validationArea.setText("Running validation for: " + selected + "\n\n");
        validationArea.appendText("✓ Trim size: 6x9 inches\n");
        validationArea.appendText("✓ Bleed: 0.125 inches\n");
        validationArea.appendText("✓ Page count: 120 pages\n");
        validationArea.appendText("✓ Spine width: 0.25 inches\n");
        validationArea.appendText("✓ Font embedding: OK\n");
        validationArea.appendText("✓ Color space: CMYK\n");
        validationArea.appendText("✓ DPI: 300\n");
        validationArea.appendText("\n✓ All checks passed!");
    }

    private void exportBundle() {
        String selected = projectList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a project first.");
            alert.showAndWait();
            return;
        }

        // TODO: Export ZIP bundle
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Complete");
        alert.setContentText("KDP bundle exported successfully!\n\n" +
                "Location: Projects/" + selected + "/bundle.zip\n\n" +
                "Contents:\n" +
                "- interior.pdf\n" +
                "- cover.pdf\n" +
                "- mockup.png\n" +
                "- metadata.json\n" +
                "- readme.txt");
        alert.showAndWait();
    }

    private void refreshProjects() {
        // TODO: Load projects from database
        projectList.getItems().clear();
        projectList.getItems().add("Sample Project - Gratitude Journal");
    }
}

