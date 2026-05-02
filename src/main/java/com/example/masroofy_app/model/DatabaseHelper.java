package com.example.masroofy_app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.masroofy_app.model.Expense;

public class DatabaseHelper {
    
    private static DatabaseHelper instance;
    
    private String pinHash;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:masroofy.db";

    private DatabaseHelper() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTablesIfNotExist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private void createTablesIfNotExist() {
        try (Statement stmt = connection.createStatement()) {
            String createExpenseTable = "CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "amount REAL, " +
                    "date TEXT, " +
                    "categoryId INTEGER)";
            stmt.execute(createExpenseTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertExpense(Expense expense) {
        String sql = "INSERT INTO expenses(amount, date, categoryId) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getDate().toString()); 
            pstmt.setInt(3, expense.getCategoryId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET amount = ?, date = ?, categoryId = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getDate().toString());
            pstmt.setInt(3, expense.getCategoryId());
            pstmt.setInt(4, expense.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getExpenses(int cycleId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses"; 
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setAmount(rs.getFloat("amount"));
                expense.setCategoryId(rs.getInt("categoryId"));
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public void saveCycle(BudgetCycle cycle) {
        // Implementation to save or update BudgetCycle in DB
    }

    public BudgetCycle getCycle() {
        return new BudgetCycle(); 
    }
}
