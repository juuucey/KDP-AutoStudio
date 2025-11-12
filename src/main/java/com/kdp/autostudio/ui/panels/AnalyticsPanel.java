package com.kdp.autostudio.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Analytics panel for tracking published book performance.
 */
public class AnalyticsPanel {
    private TextField asinInput;
    private TableView<PerformanceRow> performanceTable;

    public AnalyticsPanel() {
    }

    public BorderPane getContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section: ASIN input
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Performance Analytics");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox inputBox = new HBox(10);
        inputBox.getChildren().add(new Label("Add ASIN to track:"));
        asinInput = new TextField();
        asinInput.setPromptText("Enter Amazon ASIN (e.g., B08XYZ1234)");
        asinInput.setPrefWidth(200);

        Button addButton = new Button("Add ASIN");
        addButton.setOnAction(e -> addASIN());

        Button refreshButton = new Button("Refresh Data");
        refreshButton.setOnAction(e -> refreshAnalytics());

        inputBox.getChildren().addAll(asinInput, addButton, refreshButton);

        topSection.getChildren().addAll(titleLabel, inputBox);

        // Center: Performance table
        performanceTable = createPerformanceTable();

        // Bottom: Charts (placeholder)
        VBox chartSection = new VBox(10);
        chartSection.setPadding(new Insets(10));

        Label chartLabel = new Label("BSR Trend (Last 30 Days):");
        LineChart<Number, Number> bsrChart = createBSRChart();

        chartSection.getChildren().addAll(chartLabel, bsrChart);

        root.setTop(topSection);
        root.setCenter(performanceTable);
        root.setBottom(chartSection);

        return root;
    }

    private TableView<PerformanceRow> createPerformanceTable() {
        TableView<PerformanceRow> table = new TableView<>();

        TableColumn<PerformanceRow, String> asinCol = new TableColumn<>("ASIN");
        asinCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("asin"));

        TableColumn<PerformanceRow, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));

        TableColumn<PerformanceRow, Integer> bsrCol = new TableColumn<>("BSR");
        bsrCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("bsr"));

        TableColumn<PerformanceRow, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));

        TableColumn<PerformanceRow, Integer> reviewsCol = new TableColumn<>("Reviews");
        reviewsCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("reviews"));

        TableColumn<PerformanceRow, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rating"));

        table.getColumns().addAll(asinCol, titleCol, bsrCol, priceCol, reviewsCol, ratingCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private LineChart<Number, Number> createBSRChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days Ago");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("BSR");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100000);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Best Seller Rank Trend");
        chart.setPrefHeight(300);

        // Sample data
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Sample Book");
        series.getData().add(new XYChart.Data<>(30, 50000));
        series.getData().add(new XYChart.Data<>(20, 35000));
        series.getData().add(new XYChart.Data<>(10, 25000));
        series.getData().add(new XYChart.Data<>(0, 15000));

        chart.getData().add(series);

        return chart;
    }

    private void addASIN() {
        String asin = asinInput.getText().trim();
        if (asin.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter an ASIN.");
            alert.showAndWait();
            return;
        }

        // TODO: Add ASIN to database and fetch initial data
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("ASIN added: " + asin + "\nData will be fetched and updated daily.");
        alert.showAndWait();
        asinInput.clear();
    }

    private void refreshAnalytics() {
        // TODO: Fetch latest data for all tracked ASINs
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Refreshing analytics data...");
        alert.showAndWait();
    }

    // Simple row model for performance table
    public static class PerformanceRow {
        private String asin;
        private String title;
        private int bsr;
        private double price;
        private int reviews;
        private double rating;

        public PerformanceRow(String asin, String title, int bsr, double price, int reviews, double rating) {
            this.asin = asin;
            this.title = title;
            this.bsr = bsr;
            this.price = price;
            this.reviews = reviews;
            this.rating = rating;
        }

        public String getAsin() { return asin; }
        public String getTitle() { return title; }
        public int getBsr() { return bsr; }
        public double getPrice() { return price; }
        public int getReviews() { return reviews; }
        public double getRating() { return rating; }
    }
}

