package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;

public class BudgetManager {

    private final NotificationService notificationService = new NotificationService();

    public double calculateSafeDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        return cycle.calculateDailyLimit();
    }

    public void handleRollover(BudgetCycle cycle) {
        if (cycle != null) {
            double newLimit = cycle.calculateDailyLimit();
            cycle.setLastUpdate(java.time.LocalDate.now());
            DatabaseHelper.getInstance().saveCycle(cycle);
            System.out.println("Rollover processed. New daily limit: " + newLimit);
        }
    }

    public void recalculateAfterExpense(BudgetCycle cycle, double expenseAmount, boolean isRefund) {
        if (cycle == null) return;

        // Apply the expense first
        cycle.updateRemainingBalance(expenseAmount);

        if (checkNewDay(cycle)) {
            handleRollover(cycle);
        } else {
            DatabaseHelper.getInstance().saveCycle(cycle);
        }

        if (!isRefund) {
            if (cycle.getRemainingBalance() < 0) {
                notificationService.sendExceededAlert();
            } else if (notificationService.check80Percent(cycle)) {
                notificationService.sendWarning();
            }
        } else {
            if (cycle.getRemainingBalance() >= 0) {
                notificationService.resetExceededWarning();
            }
        }
    }

    // Overload for backward compatibility — defaults to not a refund
    public void recalculateAfterExpense(BudgetCycle cycle, double expenseAmount) {
        recalculateAfterExpense(cycle, expenseAmount, false);
    }

    public boolean checkNewDay(BudgetCycle cycle) {
        if (cycle == null) return false;
        if (cycle.getLastUpdate() == null) return false;
        return TimeService.isNewDay(cycle.getLastUpdate());
    }
}
