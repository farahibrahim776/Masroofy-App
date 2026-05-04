package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;

public class BudgetManager {

    private final NotificationService notificationService = new NotificationService();

    public double calculateSafeDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0.0f;
        return cycle.calculateDailyLimit();
    }

    public void handleRollover(BudgetCycle cycle) {
        if (cycle != null) {
            cycle.calculateDailyLimit();
            cycle.setLastUpdate(java.time.LocalDate.now());
            DatabaseHelper.getInstance().saveCycle(cycle);
        }
    }

    public void recalculateAfterExpense(BudgetCycle cycle, double expenseAmount) {
        if (cycle != null) {
            cycle.updateRemainingBalance(expenseAmount);
            DatabaseHelper.getInstance().saveCycle(cycle);

            if (cycle.getRemainingBalance() < 0) {
                notificationService.sendExceededAlert();
            } else if (notificationService.check80Percent(cycle)) {
                notificationService.sendWarning();
            }
        }
    }

    public boolean checkNewDay(BudgetCycle cycle) {
        if (cycle == null || cycle.getLastUpdate() == null) {
            return true;
        }
        return TimeService.isNewDay(cycle.getLastUpdate());
    }
}
