package com.example.masroofy_app.service;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.Controller.BudgetManager;

import java.time.LocalDate;   // FIX #17: LocalDate instead of LocalDateTime
import java.util.List;

public class ExpenseManager {

    private final BudgetManager budgetManager = new BudgetManager();

    public void addExpense(BudgetCycle activeCycle, String title, double amount, int categoryId) {
        if (activeCycle == null) {
            System.out.println("Error: No active budget cycle found!");
            return;
        }

        // FIX #5: Reject zero or negative amounts — a negative amount would ADD to the
        // remaining balance as if it were a refund, corrupting the budget silently.
        if (amount <= 0) {
            System.out.println("Error: Expense amount must be greater than zero.");
            return;
        }

        Expense e = new Expense(
                activeCycle.getId(),
                amount,
                LocalDate.now(),        // FIX #17: was LocalDateTime.now()
                categoryId
        );

        DatabaseHelper.getInstance().insertExpense(e, activeCycle.getId(), title);
        budgetManager.recalculateAfterExpense(activeCycle, amount, false);
    }

    /**
     * FIX #6: editExpense() calculates the diff (new - old) and passes it to
     * recalculateAfterExpense(). The rollover check inside recalculateAfterExpense()
     * now fires AFTER the balance update, so the edit diff is applied first and
     * the rollover never interferes with the in-flight calculation.
     */
    public void editExpense(Expense expense, double newAmount, int newCategoryId, BudgetCycle activeCycle) {
        // FIX #5: Validate the new amount too
        if (newAmount <= 0) {
            System.out.println("Error: Expense amount must be greater than zero.");
            return;
        }

        double diff = newAmount - expense.getAmount();
        expense.update(newAmount, newCategoryId);
        DatabaseHelper.getInstance().updateExpense(expense);
        // diff can be negative (cheaper edit) or positive (more expensive edit) — both are fine
        budgetManager.recalculateAfterExpense(activeCycle, diff, false);
    }

    // FIX #4: Pass isRefund=true so no false "budget exceeded" notification fires on delete
    public void deleteExpense(Expense expense, BudgetCycle activeCycle) {
        DatabaseHelper.getInstance().deleteExpense(expense.getId());
        // Negative amount = money returned to remaining balance
        budgetManager.recalculateAfterExpense(activeCycle, -expense.getAmount(), true);
    }

    public List<Expense> getAllExpenses(int cycleId) {
        return DatabaseHelper.getInstance().getExpenses(cycleId);
    }
}