package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BudgetManager {

    // US #3: Dynamic Daily Limit View
    public double calculateSafeDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), cycle.getEndDate());
        if (daysRemaining <= 0) {
            return cycle.getRemainingBalance(); // Last day
        }
        return cycle.getRemainingBalance() / (daysRemaining + 1); // +1 to include today
    }

    // US #5: Daily Rollover Management
    public void handleRollover(BudgetCycle cycle) {
        TimeService timeService = new TimeService();
        if (timeService.isNewDay(cycle.getLastUpdate())) {
            // Because the daily limit is dynamically calculated using ChronoUnit.DAYS.between(now, endDate),
            // the rollover is mathematically automatic. We just need to update the last update timestamp.
            cycle.setLastUpdate(LocalDate.now());
            // In a full implementation, you would save this updated date back to SQLite here.
        }
    }
}