package com.example.masroofy_app.service;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.Controller.BudgetManager;

import java.time.LocalDate;   
import java.util.List;

public class ExpenseManager {

    private final BudgetManager budgetManager = new BudgetManager();

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

    public void deleteExpense(Expense expense, BudgetCycle activeCycle) {
        DatabaseHelper.getInstance().deleteExpense(expense.getId());
        budgetManager.recalculateAfterExpense(activeCycle, -expense.getAmount(), true);
    }

    public List<Expense> getAllExpenses(int cycleId) {
        return DatabaseHelper.getInstance().getExpenses(cycleId);
    }
}
