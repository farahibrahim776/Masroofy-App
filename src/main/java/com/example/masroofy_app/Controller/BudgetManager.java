package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BudgetManager {
    private double amount;

    public BudgetManager(double amount) {
        this.amount = amount;
    }

    public double calculateSafeDailyLimit(BudgetCycle cycle) {
        if (cycle == null || cycle.getEndDate() == null) {
            return 0.0f;
        }
        
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), cycle.getEndDate());
        
        if (daysRemaining <= 0) {
            return cycle.getRemainingBalance();
        }
        
        return cycle.getRemainingBalance() / daysRemaining;
    }

    public void handleRollover(BudgetCycle cycle) {
        if (cycle != null) {
            cycle.calculateDailyLimit();
            cycle.setLastUpdate(java.time.LocalDate.now());
        }
    }

    public void recalculateAfterExpense(BudgetCycle cycle) {
        if (cycle != null) {
            cycle.updateRemainingBalance(amount);
        }
    }

    public boolean checkNewDay(BudgetCycle cycle) {
        if (cycle == null || cycle.getLastUpdate() == null) {
            return true;
        }
        
        LocalDate lastUpdateDate = cycle.getLastUpdate();
        LocalDate today = LocalDate.now();
        
        return !lastUpdateDate.isEqual(today);
    }
}
