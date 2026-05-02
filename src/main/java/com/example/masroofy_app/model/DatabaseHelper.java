package com.example.masroofy_app.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:masroofy.db";
    private static DatabaseHelper instance;

    private DatabaseHelper() {
        initializeDatabase();
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Create BudgetCycle Table
            stmt.execute("CREATE TABLE IF NOT EXISTS budget_cycle (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "total_allowance REAL, " +
                    "start_date TEXT, " +
                    "end_date TEXT, " +
                    "remaining_balance REAL, " +
                    "last_update TEXT, " +
                    "is_active INTEGER DEFAULT 1)");

            // Create Expense Table
            stmt.execute("CREATE TABLE IF NOT EXISTS expense (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "amount REAL, " +
                    "date TEXT, " +
                    "category_name TEXT, " +
                    "cycle_id INTEGER)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // US #1: Set Initial Budget Cycle
    public void saveCycle(BudgetCycle cycle) {
        String sql = "INSERT INTO budget_cycle(total_allowance, start_date, end_date, remaining_balance, last_update) VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, cycle.getTotalAllowance());
            pstmt.setString(2, cycle.getStartDate().toString());
            pstmt.setString(3, cycle.getEndDate().toString());
            pstmt.setDouble(4, cycle.getRemainingBalance());
            pstmt.setString(5, LocalDate.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BudgetCycle getActiveCycle() {
        String sql = "SELECT * FROM budget_cycle WHERE is_active = 1 ORDER BY id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                BudgetCycle cycle = new BudgetCycle();
                cycle.setId(rs.getInt("id"));
                cycle.setTotalAllowance(rs.getDouble("total_allowance"));
                cycle.setStartDate(LocalDate.parse(rs.getString("start_date")));
                cycle.setEndDate(LocalDate.parse(rs.getString("end_date")));
                cycle.setRemainingBalance(rs.getDouble("remaining_balance"));
                cycle.setLastUpdate(LocalDate.parse(rs.getString("last_update")));
                return cycle;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRemainingBalance(int cycleId, double newBalance) {
        String sql = "UPDATE budget_cycle SET remaining_balance = ?, last_update = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, LocalDate.now().toString());
            pstmt.setInt(3, cycleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // US #2: Rapid Expense Logging
    public void insertExpense(Expense expense, int cycleId) {
        String sql = "INSERT INTO expense(amount, date, category_name, cycle_id) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getDate().toString());
            pstmt.setString(3, expense.getCategoryName());
            pstmt.setInt(4, cycleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // US #7: Transaction History Review
    public List<Expense> getExpensesByCycle(int cycleId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expense WHERE cycle_id = ? ORDER BY date DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cycleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Expense exp = new Expense();
                exp.setId(rs.getInt("id"));
                exp.setAmount(rs.getDouble("amount"));
                exp.setDate(LocalDate.parse(rs.getString("date")));
                exp.setCategoryName(rs.getString("category_name"));
                expenses.add(exp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
}