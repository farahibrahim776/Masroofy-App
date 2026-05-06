package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;

/**
 * Controller class responsible for managing budget operations.
 * It handles calculations of daily limits, budget rollover,
 * and updates after expenses or refunds.
 */
public class BudgetManager {

    private final NotificationService notificationService = new NotificationService();

    /**
     * Calculates the safe daily spending limit based on the budget cycle.
     *
     * @param cycle the current budget cycle
     * @return the calculated safe daily limit, or 0.0 if cycle is null
     */
    public double calculateSafeDailyLimit(BudgetCycle cycle) {
        if (cycle == null) return 0.0;
        return cycle.calculateDailyLimit();
    }

    /**
     * Handles budget rollover when a new day starts.
     * Updates the last update date and saves the cycle to the database.
     *
     * @param cycle the current budget cycle
     */
    public void handleRollover(BudgetCycle cycle) {
        if (cycle != null) {
            double newLimit = cycle.calculateDailyLimit();
            cycle.setLastUpdate(java.time.LocalDate.now());
            DatabaseHelper.getInstance().saveCycle(cycle);
            System.out.println("Rollover processed. New daily limit: " + newLimit);
        }
    }

    /**
     * Recalculates the budget after an expense or refund.
     * Updates remaining balance, checks for rollover, and triggers notifications
     * based on budget status.
     *
     * @param cycle the current budget cycle
     * @param expenseAmount the amount of the expense
     * @param isRefund true if the transaction is a refund, false if it is an expense
     */
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

    /**
     * Overloaded method for recalculating budget after an expense.
     * Assumes the transaction is not a refund.
     *
     * @param cycle the current budget cycle
     * @param expenseAmount the expense amount
     */
    // Overload for backward compatibility — defaults to not a refund
    public void recalculateAfterExpense(BudgetCycle cycle, double expenseAmount) {
        recalculateAfterExpense(cycle, expenseAmount, false);
    }


    /**
     * Checks whether a new day has started compared to the last update.
     *
     * @param cycle the current budget cycle
     * @return true if a new day has started, false otherwise
     */
    public boolean checkNewDay(BudgetCycle cycle) {
        if (cycle == null) return false;
        if (cycle.getLastUpdate() == null) return false;
        return TimeService.isNewDay(cycle.getLastUpdate());
    }
}
