package com.kdp.autostudio.ui;

import com.kdp.autostudio.ui.panels.*;
import com.kdp.autostudio.config.ConfigManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main application window with tabbed interface for different panels.
 */
public class MainWindow {
    private Stage stage;
    private TabPane tabPane;

    public MainWindow(Stage stage) {
        this.stage = stage;
        initializeUI();
    }

    private void initializeUI() {
        stage.setTitle("KDP AutoStudio - AI-Powered Book Creation Studio");

        // Create main layout
        BorderPane root = new BorderPane();

        // Create menu bar
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Create tabbed interface
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Add panels
        Tab researchTab = new Tab("Research & Ideas", new ResearchPanel().getContent());
        Tab backlogTab = new Tab("Idea Backlog", new BacklogPanel().getContent());
        Tab productionTab = new Tab("Production", new ProductionPanel().getContent());
        Tab packageTab = new Tab("Package & Export", new PackagePanel().getContent());
        Tab analyticsTab = new Tab("Analytics", new AnalyticsPanel().getContent());

        tabPane.getTabs().addAll(researchTab, backlogTab, productionTab, packageTab, analyticsTab);

        root.setCenter(tabPane);

        // Create scene
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem settingsItem = new MenuItem("Settings");
        settingsItem.setOnAction(e -> showSettingsDialog());
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> stage.close());
        fileMenu.getItems().addAll(settingsItem, new SeparatorMenuItem(), exitItem);

        // Tools menu
        Menu toolsMenu = new Menu("Tools");
        MenuItem validateItem = new MenuItem("Validate Projects");
        MenuItem exportItem = new MenuItem("Export All");
        toolsMenu.getItems().addAll(validateItem, exportItem);

        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, toolsMenu, helpMenu);
        return menuBar;
    }

    private void showSettingsDialog() {
        ConfigManager configManager = ConfigManager.getInstance();
        
        // Create dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Settings");
        dialog.setHeaderText("Configure KDP AutoStudio");
        
        // Create form
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setPrefWidth(500);
        
        // OpenAI API Key section
        Label apiKeyLabel = new Label("OpenAI API Key:");
        PasswordField apiKeyField = new PasswordField();
        String currentKey = configManager.getOpenAIApiKey();
        if (currentKey != null && !currentKey.isEmpty()) {
            // Show masked version
            apiKeyField.setText("••••••••••••" + currentKey.substring(Math.max(0, currentKey.length() - 4)));
        }
        apiKeyField.setPromptText("sk-...");
        
        HBox apiKeyBox = new HBox(10);
        apiKeyBox.getChildren().addAll(apiKeyLabel, apiKeyField);
        HBox.setHgrow(apiKeyField, Priority.ALWAYS);
        
        // Help text for API key
        Label apiKeyHelp = new Label("Get your API key from: https://platform.openai.com/api-keys");
        apiKeyHelp.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");
        
        // Scoring Weights section
        Label weightsLabel = new Label("Idea Scoring Weights:");
        weightsLabel.setStyle("-fx-font-weight: bold;");
        
        com.kdp.autostudio.config.ScoringWeights weights = configManager.getScoringWeights();
        
        Spinner<Double> demandSpinner = new Spinner<>(0.0, 1.0, weights.getDemand(), 0.05);
        demandSpinner.setEditable(true);
        Spinner<Double> competitionSpinner = new Spinner<>(0.0, 1.0, weights.getCompetition(), 0.05);
        competitionSpinner.setEditable(true);
        Spinner<Double> marginSpinner = new Spinner<>(0.0, 1.0, weights.getMargin(), 0.05);
        marginSpinner.setEditable(true);
        Spinner<Double> effortSpinner = new Spinner<>(0.0, 1.0, weights.getEffort(), 0.05);
        effortSpinner.setEditable(true);
        Spinner<Double> noveltySpinner = new Spinner<>(0.0, 1.0, weights.getNovelty(), 0.05);
        noveltySpinner.setEditable(true);
        
        VBox weightsBox = new VBox(8);
        weightsBox.getChildren().addAll(
            createWeightRow("Demand:", demandSpinner),
            createWeightRow("Competition:", competitionSpinner),
            createWeightRow("Margin:", marginSpinner),
            createWeightRow("Effort:", effortSpinner),
            createWeightRow("Novelty:", noveltySpinner)
        );
        
        // Paths section (read-only display)
        Label pathsLabel = new Label("Paths:");
        pathsLabel.setStyle("-fx-font-weight: bold;");
        
        TextField projectsPathField = new TextField(configManager.getProjectsPath());
        projectsPathField.setEditable(false);
        TextField templatesPathField = new TextField(configManager.getTemplatesPath());
        templatesPathField.setEditable(false);
        TextField assetsPathField = new TextField(configManager.getAssetsPath());
        assetsPathField.setEditable(false);
        
        VBox pathsBox = new VBox(8);
        pathsBox.getChildren().addAll(
            createPathRow("Projects:", projectsPathField),
            createPathRow("Templates:", templatesPathField),
            createPathRow("Assets:", assetsPathField)
        );
        
        // Assemble form
        form.getChildren().addAll(
            apiKeyBox,
            apiKeyHelp,
            new Separator(),
            weightsLabel,
            weightsBox,
            new Separator(),
            pathsLabel,
            pathsBox
        );
        
        dialog.getDialogPane().setContent(form);
        
        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        // Handle save
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                // Save API key (only if changed)
                String newApiKey = apiKeyField.getText();
                if (!newApiKey.isEmpty() && !newApiKey.startsWith("••••")) {
                    configManager.setOpenAIApiKey(newApiKey);
                }
                
                // Save scoring weights
                com.kdp.autostudio.config.ScoringWeights newWeights = new com.kdp.autostudio.config.ScoringWeights(
                    demandSpinner.getValue(),
                    competitionSpinner.getValue(),
                    marginSpinner.getValue(),
                    effortSpinner.getValue(),
                    noveltySpinner.getValue()
                );
                configManager.setScoringWeights(newWeights);
                
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Settings Saved");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Settings have been saved successfully.");
                successAlert.showAndWait();
            }
            return buttonType;
        });
        
        dialog.showAndWait();
    }
    
    private HBox createWeightRow(String label, Spinner<Double> spinner) {
        HBox row = new HBox(10);
        Label labelControl = new Label(label);
        labelControl.setPrefWidth(100);
        spinner.setPrefWidth(100);
        row.getChildren().addAll(labelControl, spinner);
        return row;
    }
    
    private HBox createPathRow(String label, TextField textField) {
        HBox row = new HBox(10);
        Label labelControl = new Label(label);
        labelControl.setPrefWidth(80);
        HBox.setHgrow(textField, Priority.ALWAYS);
        row.getChildren().addAll(labelControl, textField);
        return row;
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About KDP AutoStudio");
        alert.setHeaderText("KDP AutoStudio v1.0.0");
        alert.setContentText("AI-powered desktop research & design studio for Amazon KDP creators.\n\n" +
                "Automate your entire KDP book creation pipeline from research to production.");
        alert.showAndWait();
    }

    public void show() {
        stage.show();
    }
}

