package com.example.masroofy_app.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;

/**
 * Service class responsible for preparing dashboard data.
 * It calculates financial summaries such as remaining balance,
 * daily limit, and category-based expense distribution.
 */
public class DashboardManager {

    /**
     * Retrieves the remaining balance from the current budget cycle.
     *
     * @param cycle the active budget cycle
     * @return remaining balance, or 0.0 if cycle is null
     */
    public double getRemainingBalance(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        return cycle.getRemainingBalance();
    }

    /**
     * Calculates the daily spending limit based on the current cycle.
     *
     * @param cycle the active budget cycle
     * @return daily limit, or 0.0 if cycle is null
     */
    public double getDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        return cycle.calculateDailyLimit();
    }

    /**
     * Generates a summary of expenses grouped by category.
     *
     * @param expenses list of expenses
     * @return a map where:
     *         key = category ID,
     *         value = total amount spent in that category
     */
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
