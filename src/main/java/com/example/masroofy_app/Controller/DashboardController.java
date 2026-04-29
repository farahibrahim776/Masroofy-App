package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.Category;
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
    private List<Category> categories;
    // ===== Initialize =====
    @FXML
    public void initialize() {
        pieChart.getData().addAll(
                new PieChart.Data("Food", 50),
                new PieChart.Data("Transport", 30),
                new PieChart.Data("Entertainment", 20)
        );
    }

    // ===== Receive Data from App =====
    private Map<Integer, String> categoryMap = new HashMap<>();

    public void setData(BudgetCycle cycle, List<Expense> expensesList, List<Category> categoryList) {
        this.currentCycle = cycle;
        this.expenses = expensesList;
        this.categories = categoryList;
        categoryMap.clear();

        // Build Map
        if (categories != null) {
            for (Category c : categories) {
                categoryMap.put(c.getId(), c.getName());
            }
        }

        loadDashboard();
    }

    private String getCategoryNameById(int id) {
        return categoryMap.getOrDefault(id, "Unknown");
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
                    new PieChart.Data(categoryName + " (" + amount + ")", amount) );
        }
    }
    public void handleLogExpense(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/AddExpense.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void goToDashboard(ActionEvent event) {
        System.out.println("Already in Dashboard");
    }
    @FXML
    private void goToHistory(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/HistoryUI.fxml")
            );

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToStats(javafx.event.ActionEvent event) {
        System.out.println("Go to Stats");
    }
    @FXML
    private void goToSettings(javafx.event.ActionEvent event) {
        System.out.println("Go to Settings");
    }

}