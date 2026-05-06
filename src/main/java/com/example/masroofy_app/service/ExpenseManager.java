package com.example.masroofy_app.service;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.Controller.BudgetManager;

import java.time.LocalDate;   
import java.util.List;

/**
 * Service class responsible for managing all expense operations.
 * It handles adding, editing, deleting expenses, and updating the budget accordingly.
 */
public class ExpenseManager {

    private final BudgetManager budgetManager = new BudgetManager();

    /**
     * Adds a new expense to the database and updates the budget cycle.
     *
     * @param activeCycle the current active budget cycle
     * @param title title/description of the expense
     * @param amount expense amount (must be > 0)
     * @param categoryId category ID of the expense
     */
    public void addExpense(BudgetCycle activeCycle, String title, double amount, int categoryId) {
        if (activeCycle == null) {
            System.out.println("Error: No active budget cycle found!");
            return;
        }

        if (amount <= 0) {
            System.out.println("Error: Expense amount must be greater than zero.");
            return;
        }

        Expense e = new Expense(
                activeCycle.getId(),
                amount,
                LocalDate.now(),
                categoryId
        );

        DatabaseHelper.getInstance().insertExpense(e, activeCycle.getId(), title);
        budgetManager.recalculateAfterExpense(activeCycle, amount, false);
    }

    /**
     * Edits an existing expense and updates the budget accordingly.
     *
     * @param expense the expense to edit
     * @param newAmount new amount value
     * @param newCategoryId new category ID
     * @param activeCycle the current budget cycle
     */
    public void editExpense(Expense expense, double newAmount, int newCategoryId, BudgetCycle activeCycle) {
        if (newAmount <= 0) {
            System.out.println("Error: Expense amount must be greater than zero.");
            return;
        }

        double diff = newAmount - expense.getAmount();
        expense.update(newAmount, newCategoryId);
        DatabaseHelper.getInstance().updateExpense(expense);
        budgetManager.recalculateAfterExpense(activeCycle, diff, false);
    }

    /**
     * Deletes an expense and restores its amount to the budget.
     *
     * @param expense the expense to delete
     * @param activeCycle the current budget cycle
     */
    public void deleteExpense(Expense expense, BudgetCycle activeCycle) {
        DatabaseHelper.getInstance().deleteExpense(expense.getId());
        budgetManager.recalculateAfterExpense(activeCycle, -expense.getAmount(), true);
    }
    /**
     * Retrieves all expenses for a given cycle.
     *
     * @param cycleId the ID of the budget cycle
     * @return list of expenses
     */
    public List<Expense> getAllExpenses(int cycleId) {
        return DatabaseHelper.getInstance().getExpenses(cycleId);
    }
}
