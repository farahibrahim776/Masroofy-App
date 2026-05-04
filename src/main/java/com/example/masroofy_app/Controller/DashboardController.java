package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.service.DashboardManager;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DashboardController {

    private final DashboardManager dashboardManager = new DashboardManager();

    @FXML
    private Label dailyLimitLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label daysLabel;

    @FXML
    private PieChart pieChart;

    private BudgetCycle currentCycle;
    private List<Expense> expenses;
    private final Map<Integer, String> categoryMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialize default categories mapping (matching your DB.java)
        categoryMap.put(1, "Food");
        categoryMap.put(2, "Transport");
        categoryMap.put(3, "Shopping");
        categoryMap.put(4, "Bills");
        categoryMap.put(5, "Entertainment");
        categoryMap.put(6, "Other");

        // Fetch the active cycle from the database
        this.currentCycle = DatabaseHelper.getInstance().getCycle();

        if (this.currentCycle != null) {
            // Fetch real expenses for the active cycle
            this.expenses = DatabaseHelper.getInstance().getExpenses(this.currentCycle.getId());
            loadDashboard();
        } else {
            System.out.println("No active budget cycle found.");
            dailyLimitLabel.setText("0.00 EGP");
            balanceLabel.setText("0.00 EGP");
            daysLabel.setText("0 Days Left");
        }
    }

    private String getCategoryNameById(int id) {
        return categoryMap.getOrDefault(id, "Other");
    }

    private void loadDashboard() {
        if (currentCycle == null) return;

        // Remaining Balance
        float remaining = dashboardManager.getRemainingBalance(currentCycle);
        balanceLabel.setText(String.format("%.2f EGP", remaining));

        // Daily Limit
        float dailyLimit = dashboardManager.getDailyLimit(currentCycle);
        dailyLimitLabel.setText(String.format("%.2f EGP", dailyLimit));

        // Days Left
        long daysLeft = currentCycle.getRemainingDays();
        daysLabel.setText(daysLeft + " Days Left");

        // Pie Chart
        loadPieChart();
    }

    private void loadPieChart() {
        if (expenses == null || expenses.isEmpty()) {
            pieChart.getData().clear();
            return;
        }

        Map<Integer, Float> data = dashboardManager.generateCategorySummary(expenses);
        pieChart.getData().clear();

        for (Map.Entry<Integer, Float> entry : data.entrySet()) {
            String categoryName = getCategoryNameById(entry.getKey());
            float amount = entry.getValue();
            pieChart.getData().add(new PieChart.Data(categoryName + " (" + amount + ")", amount));
        }
    }

    // ===== Navigation Methods =====

    @FXML
    public void handleLogExpense(ActionEvent event) {
        navigateTo(event, "/view/NewExpenseUI.fxml");
    }

    @FXML
    private void goToHistory(ActionEvent event) {
        navigateTo(event, "/view/HistoryUI.fxml");
    }

    @FXML
    private void goToStats(ActionEvent event) {
        navigateTo(event, "/view/StatsUI.fxml");
    }

    @FXML
    private void goToSettings(ActionEvent event) {
        navigateTo(event, "/view/SettingsUI.fxml");
    }

    // Helper method to eliminate duplicate navigation code
    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
