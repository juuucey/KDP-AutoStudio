package com.kdp.autostudio.ui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Backlog panel for viewing, sorting, and approving book ideas.
 */
public class BacklogPanel {
    private TableView<IdeaRow> ideaTable;
    private ObservableList<IdeaRow> ideas;

    public BacklogPanel() {
        ideas = FXCollections.observableArrayList();
        // TODO: Load ideas from database
    }

    public BorderPane getContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section: Filters and actions
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Idea Backlog");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Score (High to Low)", "Score (Low to High)", "Demand", "Competition", "Date");
        sortCombo.setValue("Score (High to Low)");

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All", "Pending", "Approved", "Rejected");
        filterCombo.setValue("All");

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshIdeas());

        topBox.getChildren().addAll(titleLabel, new Label("Sort:"), sortCombo, new Label("Filter:"), filterCombo, refreshButton);

        // Center: Table view
        ideaTable = createIdeaTable();
        ideaTable.setItems(ideas);

        // Bottom: Actions
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));

        Button approveButton = new Button("Approve Selected");
        approveButton.setOnAction(e -> approveSelected());
        Button rejectButton = new Button("Reject Selected");
        rejectButton.setOnAction(e -> rejectSelected());
        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setOnAction(e -> viewDetails());

        bottomBox.getChildren().addAll(approveButton, rejectButton, viewDetailsButton);

        root.setTop(topBox);
        root.setCenter(ideaTable);
        root.setBottom(bottomBox);

        return root;
    }

    private TableView<IdeaRow> createIdeaTable() {
        TableView<IdeaRow> table = new TableView<>();

        TableColumn<IdeaRow, String> keywordCol = new TableColumn<>("Keyword");
        keywordCol.setCellValueFactory(new PropertyValueFactory<>("keyword"));

        TableColumn<IdeaRow, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<IdeaRow, Double> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<IdeaRow, Double> demandCol = new TableColumn<>("Demand");
        demandCol.setCellValueFactory(new PropertyValueFactory<>("demand"));

        TableColumn<IdeaRow, Double> competitionCol = new TableColumn<>("Competition");
        competitionCol.setCellValueFactory(new PropertyValueFactory<>("competition"));

        TableColumn<IdeaRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(keywordCol, titleCol, scoreCol, demandCol, competitionCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void refreshIdeas() {
        // TODO: Load ideas from database
        ideas.clear();
        // Add sample data for demonstration
        ideas.add(new IdeaRow("gratitude journal", "Gratitude Journal", 0.85, 0.75, 0.60, "pending"));
    }

    private void approveSelected() {
        IdeaRow selected = ideaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // TODO: Update status in database
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Idea approved: " + selected.getKeyword());
            alert.showAndWait();
        }
    }

    private void rejectSelected() {
        IdeaRow selected = ideaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // TODO: Update status in database
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Idea rejected: " + selected.getKeyword());
            alert.showAndWait();
        }
    }

    private void viewDetails() {
        IdeaRow selected = ideaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // TODO: Show detailed view with AI explanation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Idea Details");
            alert.setHeaderText(selected.getTitle());
            alert.setContentText("AI Explanation and detailed metrics will be shown here.");
            alert.showAndWait();
        }
    }

    // Simple row model for table display
    public static class IdeaRow {
        private String keyword;
        private String title;
        private double score;
        private double demand;
        private double competition;
        private String status;

        public IdeaRow(String keyword, String title, double score, double demand, double competition, String status) {
            this.keyword = keyword;
            this.title = title;
            this.score = score;
            this.demand = demand;
            this.competition = competition;
            this.status = status;
        }

        public String getKeyword() { return keyword; }
        public String getTitle() { return title; }
        public double getScore() { return score; }
        public double getDemand() { return demand; }
        public double getCompetition() { return competition; }
        public String getStatus() { return status; }
    }
}

