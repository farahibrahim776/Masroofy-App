package com.example.masroofy_app.Controller;

import com.example.masroofy_app.navigation.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class StatsController {

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalLabel;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        totalLabel.setText("400 EGP");

        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Food", 150));
        pieChart.getData().add(new PieChart.Data("Transportation", 100));
        pieChart.getData().add(new PieChart.Data("Entertainment", 150));
    }

    // ===== Navigation (Clean Version) =====

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneManager.switchScene("/view/DashboardUI.fxml");
    }

    @FXML
    private void handleHistory(ActionEvent event) {
        SceneManager.switchScene("/view/HistoryUI.fxml");
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        SceneManager.switchScene("/view/SettingsUI.fxml");
    }
}