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
            // Rollover policy: the daily limit recalculates from the current remaining balance
            // spread over remaining days. Unused daily allowance carries forward automatically
            // because remainingBalance is never reset — only the daily limit recalculates.
            double newLimit = cycle.calculateDailyLimit();
            cycle.setLastUpdate(java.time.LocalDate.now());
            DatabaseHelper.getInstance().saveCycle(cycle);
            System.out.println("Rollover processed. New daily limit: " + newLimit);
        }
    }

    /**
     * FIX #4: isRefund=true skips notification checks (used for deletions).
     * FIX #6: Rollover is checked AFTER applying the expense diff, not before,
     *         to avoid the rollover recalculating the daily limit mid-edit.
     *         The rollover only updates lastUpdate and persists — it does NOT
     *         alter remainingBalance, so order no longer affects correctness.
     */
    public void recalculateAfterExpense(BudgetCycle cycle, double expenseAmount, boolean isRefund) {
        if (cycle == null) return;

        // Apply the expense first
        cycle.updateRemainingBalance(expenseAmount);

        // Then check if a new day started and persist the updated daily limit
        // FIX #6: Rollover fires after the balance update, so it never interferes with the diff
        if (checkNewDay(cycle)) {
            handleRollover(cycle);
        } else {
            DatabaseHelper.getInstance().saveCycle(cycle);
        }

        // FIX #4: Only send notifications for real new expenses, not refunds/deletions
        if (!isRefund) {
            if (cycle.getRemainingBalance() < 0) {
                notificationService.sendExceededAlert();
            } else if (notificationService.check80Percent(cycle)) {
                notificationService.sendWarning();
            }
        } else {
            // FIX #11: If a deletion brings the balance back above thresholds, reset flags
            // so the user gets warned again if they overspend again in the same session.
            if (cycle.getRemainingBalance() >= 0) {
                notificationService.resetExceededWarning();
            }
        }
    }

    // Overload for backward compatibility — defaults to not a refund
    public void recalculateAfterExpense(BudgetCycle cycle, double expenseAmount) {
        recalculateAfterExpense(cycle, expenseAmount, false);
    }

    /**
     * FIX #4 & #8: Uses TimeService (the dedicated utility class) instead of
     * duplicating the logic inline. lastUpdate is guaranteed non-null by
     * BudgetCycle.fromDatabase(), so the null branch here is a safe last resort only.
     */
    public boolean checkNewDay(BudgetCycle cycle) {
        if (cycle == null) return false;
        // lastUpdate is never null after BudgetCycle.fromDatabase() fix, but guard anyway
        if (cycle.getLastUpdate() == null) return false;
        return TimeService.isNewDay(cycle.getLastUpdate());
    }
}