package com.example.masroofy_app.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;

public class DashboardManager {

    public float getRemainingBalance(BudgetCycle cycle) {
        if (cycle == null) return 0f;

        return (float) cycle.getRemainingBalance();
    }

    public float getDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0f;

        long daysLeft = cycle.getRemainingDays();

        if (daysLeft <= 0) {
            return 0f;
        }

        return getRemainingBalance(cycle) / daysLeft;
    }

    // PieChart data
    public Map<Integer, Float> generateCategorySummary(List<Expense> expenses) {

        Map<Integer, Float> summary = new HashMap<>();

        if (expenses == null) return summary;

        for (Expense e : expenses) {

            int categoryId = e.getCategoryId();
            float amount = (float) e.getAmount();

            summary.put(categoryId,
                    summary.getOrDefault(categoryId, 0f) + amount);
        }

        return summary;
    }
}