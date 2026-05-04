package com.example.masroofy_app.model;

import com.example.masroofy_app.DB;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {
        this.connection = DB.connect();
        DB.initDatabase();
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private String hashPin(String pin) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing PIN", e);
        }
    }

    public boolean isPinSetup() {
        String sql = "SELECT COUNT(*) FROM users WHERE pin_hash IS NOT NULL";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void savePin(String pin) {
        String hashedPin = hashPin(pin);
        try {
            if (isPinSetup()) {
                String updateSql = "UPDATE users SET pin_hash = ? WHERE id = 1";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
                    pstmt.setString(1, hashedPin);
                    pstmt.executeUpdate();
                }
            } else {
                String insertSql = "UPDATE users SET pin_hash = ? WHERE id = 1";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    pstmt.setString(1, hashedPin);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyPin(String inputPin) {
        String hashedInput = hashPin(inputPin);
        String sql = "SELECT pin_hash FROM users LIMIT 1";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String storedHash = rs.getString("pin_hash");
                return hashedInput.equals(storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertExpense(Expense expense, int activeCycleId, String title) {
        String sql = "INSERT INTO expenses(title, category, amount, date, cycle_id) VALUES(?, ?, ?, ?, ?)"; //[cite: 8]
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) { 
            pstmt.setString(1, title); 
            pstmt.setString(2, String.valueOf(expense.getCategoryId()));
            pstmt.setDouble(3, expense.getAmount()); 
            pstmt.setString(4, expense.getDate().toString());
            pstmt.setInt(5, activeCycleId); 
            pstmt.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }

    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET amount = ?, date = ?, category = ? WHERE id = ?"; 
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) { 
            pstmt.setDouble(1, expense.getAmount()); 
            pstmt.setString(2, expense.getDate().toString()); 
            pstmt.setString(3, String.valueOf(expense.getCategoryId())); 
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
        String sql = "SELECT * FROM expenses WHERE cycle_id = ?"; 
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) { 
            pstmt.setInt(1, cycleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                float amount = rs.getFloat("amount"); 
                
                int catId = 0; 
                try {
                    catId = Integer.parseInt(rs.getString("category"));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Category is not a valid integer format.");
                }

                String dateString = rs.getString("date");
                LocalDateTime parsedDate = LocalDateTime.parse(dateString);

                Expense expense = new Expense(cycleId, amount, parsedDate, catId);
                expense.setId(rs.getInt("id")); 
                expenses.add(expense); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return expenses; 
    }

    public void saveCycle(BudgetCycle cycle) {
        String deactivateSql = "UPDATE budget_cycle SET active = 0";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deactivateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertSql = "INSERT INTO budget_cycle(total_allowance, start_date, end_date, remaining_balance, daily_limit, active) VALUES(?, ?, ?, ?, ?, 1)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setDouble(1, cycle.getTotalAllowance());
            pstmt.setString(2, cycle.getStartDate().toString()); 
            pstmt.setString(3, cycle.getEndDate().toString());
            pstmt.setDouble(4, cycle.getRemainingBalance());
            pstmt.setDouble(5, cycle.calculateDailyLimit());
            pstmt.executeUpdate();
            System.out.println("Cycle successfully saved to database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BudgetCycle getCycle() {
        String sql = "SELECT * FROM budget_cycle WHERE active = 1 ORDER BY id DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int id = rs.getInt("id");
                double totalAllowance = rs.getDouble("total_allowance");

                LocalDate startDate = java.time.LocalDate.parse(rs.getString("start_date"));
                LocalDate endDate = java.time.LocalDate.parse(rs.getString("end_date"));
                
                BudgetCycle cycle = new BudgetCycle(id, totalAllowance, startDate, endDate);

                double dbRemaining = rs.getDouble("remaining_balance");
                double amountSpentSoFar = totalAllowance - dbRemaining;
                if (amountSpentSoFar > 0) {
                    cycle.updateRemainingBalance(amountSpentSoFar);
                }
                return cycle;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public void setPrivacyEnabled(boolean isEnabled) {
        int value = isEnabled ? 1 : 0;
        String sql = "UPDATE users SET privacy_enabled = ? WHERE id = 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPrivacyEnabled() {
        String sql = "SELECT privacy_enabled FROM users LIMIT 1";
        
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("privacy_enabled") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
