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

    /**
     * FIX #7: Removed duplicate daily-limit calculation.
     * BudgetCycle.calculateDailyLimit() is now the single source of truth —
     * it already rounds to 2 decimal places, so we don't need to repeat that here.
     * Previously this method had its own rounding (Math.round / 100.0) that differed
     * subtly from BudgetCycle's unrounded version, causing display vs. storage drift.
     */
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