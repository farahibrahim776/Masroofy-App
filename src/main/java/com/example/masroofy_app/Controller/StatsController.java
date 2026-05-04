package com.example.masroofy_app.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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

    public void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/masroofy_app/" + fxmlFile)
            );

            Parent root = loader.load();

            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardUI.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHistory(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/HistoryUI.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/SettingsUI.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
