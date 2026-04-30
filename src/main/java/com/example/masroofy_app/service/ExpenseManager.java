package com.example.masroofy_app.service;

import com.example.masroofy_app.model.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {

    private static List<Expense> expenses = new ArrayList<>();

    // Add Expense
    public void addExpense(double amount, int categoryId) {
        int id = expenses.size() + 1;

        Expense e = new Expense(
                id,
                amount,
                java.time.LocalDateTime.now(),
                categoryId
        );

        expenses.add(e);
    }

    // Edit Expense
    public void editExpense(int id, double amount, int categoryId) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                e.update(amount, categoryId);
                return;
            }
        }
    }

    // Delete Expense
    public void deleteExpense(int id) {
        expenses.removeIf(e -> e.getId() == id);
    }

    // Get All Expenses
    public List<Expense> getAllExpenses() {
        return expenses;
    }
    public void addExpense(double amount, int categoryId, java.time.LocalDateTime date) {
        expenses.add(
                new Expense(
                        expenses.size() + 1,
                        amount,
                        date,
                        categoryId
                )
        );
    }
}