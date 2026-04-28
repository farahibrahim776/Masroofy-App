package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.Category;
import com.example.masroofy_app.service.DashboardManager;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

public class DashboardController {

    private final DashboardManager dashboardManager = new DashboardManager();

    // ===== UI Elements =====
    @FXML
    private Label dailyLimitLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label daysLabel;

    @FXML
    private PieChart pieChart;

    // ===== Data =====
    private BudgetCycle currentCycle;
    private List<Expense> expenses;

    // ===== Initialize =====
    @FXML
    public void initialize() {
        // ممكن تسيبيها فاضية أو تعملي default UI
    }

    // ===== Receive Data from App =====
    public void setData(BudgetCycle cycle, List<Expense> expensesList) {
        this.currentCycle = cycle;
        this.expenses = expensesList;

        loadDashboard();
    }

    // ===== Load All Dashboard Data =====
    private void loadDashboard() {

        if (currentCycle == null) return;

        // Remaining Balance
        float remaining = dashboardManager.getRemainingBalance(currentCycle);
        showBalance(remaining);

        // Daily Limit
        float dailyLimit = dashboardManager.getDailyLimit(currentCycle);
        showDailyLimit(dailyLimit);

        // Days Left
        long daysLeft = currentCycle.getRemainingDays();
        showDaysLeft((int) daysLeft);

        // Pie Chart
        loadPieChart();
    }

    // ===== UI Update Methods =====

    private void showDailyLimit(float limit) {
        dailyLimitLabel.setText(limit + " EGP");
    }

    private void showBalance(float balance) {
        balanceLabel.setText(balance + " EGP");
    }

    private void showDaysLeft(int days) {
        daysLabel.setText(String.valueOf(days));
    }

    private void loadPieChart() {

        if (expenses == null) return;

        Map<Integer, Float> data =
                dashboardManager.generateCategorySummary(expenses);

        pieChart.getData().clear();

        for (Map.Entry<Integer, Float> entry : data.entrySet()) {

            String categoryName =getCategoryNameById(entry.getKey());
            float amount = entry.getValue();

            pieChart.getData().add(
                    new PieChart.Data(categoryName, amount)
            );
        }
    }
}