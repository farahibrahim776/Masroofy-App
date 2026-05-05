package com.example.masroofy_app.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;

public class DashboardManager {

    public double getRemainingBalance(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        return cycle.getRemainingBalance();
    }

    public double getDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        return cycle.calculateDailyLimit();
    }

    public Map<Integer, Double> generateCategorySummary(List<Expense> expenses) {
        Map<Integer, Double> summary = new HashMap<>();
        if (expenses == null) return summary;

        for (Expense e : expenses) {
            int categoryId = e.getCategoryId();
            double amount = e.getAmount();
            summary.put(categoryId, summary.getOrDefault(categoryId, 0.0) + amount);
        }

        return summary;
    }
}
