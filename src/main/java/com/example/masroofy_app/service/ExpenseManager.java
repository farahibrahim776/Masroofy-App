package com.example.masroofy_app.service;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.Controller.BudgetManager;

import java.time.LocalDateTime;
import java.util.List;

public class ExpenseManager {

    private final BudgetManager budgetManager = new BudgetManager();

    public void addExpense(BudgetCycle activeCycle, String title, double amount, int categoryId) {
        if (activeCycle == null) {
            System.out.println("Error: No active budget cycle found!");
            return;
        }

        Expense e = new Expense(
                activeCycle.getId(),
                amount,
                LocalDateTime.now(),
                categoryId
        );

        DatabaseHelper.getInstance().insertExpense(e, activeCycle.getId(), title);

        budgetManager.recalculateAfterExpense(activeCycle, amount);
    }

    // Edit Expense
    public void editExpense(Expense expense, double newAmount, int newCategoryId) {
        expense.update(newAmount, newCategoryId);
        DatabaseHelper.getInstance().updateExpense(expense);
    }

    // Delete Expense
    public void deleteExpense(int expenseId) {
        DatabaseHelper.getInstance().deleteExpense(expenseId);
    }

    // Get All Expenses for the active cycle
    public List<Expense> getAllExpenses(int cycleId) {
        return DatabaseHelper.getInstance().getExpenses(cycleId);
    }
}
