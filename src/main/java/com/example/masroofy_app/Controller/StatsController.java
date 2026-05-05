package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.navigation.SceneManager;
import com.example.masroofy_app.utils.CategoryUtils;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        pieChart.getData().clear();
        double totalExpenses = 0.0;

        BudgetCycle currentCycle = DatabaseHelper.getInstance().getCycle();

        if (currentCycle != null) {
            List<Expense> expenses = DatabaseHelper.getInstance().getExpenses(currentCycle.getId());

            Map<Integer, Double> categoryTotals = new HashMap<>();

            for (Expense expense : expenses) {
                double amount = expense.getAmount();
                totalExpenses += amount;

                int catId = expense.getCategoryId();
                categoryTotals.put(catId, categoryTotals.getOrDefault(catId, 0.0) + amount);
            }

            for (Map.Entry<Integer, Double> entry : categoryTotals.entrySet()) {
                String categoryName = CategoryUtils.getCategoryName(entry.getKey());
                pieChart.getData().add(new PieChart.Data(categoryName, entry.getValue()));
            }
        }

        totalLabel.setText(String.format("%.2f EGP", totalExpenses));
    }

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
