package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.service.DashboardManager;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.example.masroofy_app.navigation.SceneManager;

import java.util.List;
import java.util.Map;

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

    @FXML
    public void initialize() {
        this.currentCycle = DatabaseHelper.getInstance().getCycle();
    
        if (this.currentCycle != null) {
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
        return com.example.masroofy_app.utils.CategoryUtils.getCategoryName(id);
    }

    private void loadDashboard() {
        if (currentCycle == null) return;

        // Remaining Balance
        double remaining = dashboardManager.getRemainingBalance(currentCycle);
        balanceLabel.setText(String.format("%.2f EGP", remaining));

        // Daily Limit
        double dailyLimit = dashboardManager.getDailyLimit(currentCycle);
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

        Map<Integer, Double> data = dashboardManager.generateCategorySummary(expenses);
        pieChart.getData().clear();

        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            String categoryName = getCategoryNameById(entry.getKey());
            double amount = entry.getValue();
            pieChart.getData().add(new PieChart.Data(categoryName + " (" + amount + ")", amount));
        }
    }

    // ===== Navigation Methods =====

    @FXML
    public void handleLogExpense(ActionEvent event) {
        SceneManager.switchScene("/view/NewExpenseUI.fxml");
    }

    @FXML
    public void goToHistory(ActionEvent event) {
        SceneManager.switchScene("/view/HistoryUI.fxml");
    }

    @FXML
    public void goToStats(ActionEvent event) {
        SceneManager.switchScene("/view/StatsUI.fxml");
    }

    @FXML
    public void goToSettings(ActionEvent event) {
        SceneManager.switchScene("/view/SettingsUI.fxml");
    }
}