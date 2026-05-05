package com.example.masroofy_app.model;

import com.example.masroofy_app.DB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// FIX #13: Removed AutoCloseable — a singleton is never closed in try-with-resources.
// Implementing AutoCloseable on a singleton implies it's safe to close it, which would
// destroy the shared connection for every other class. JVM shutdown hook is used instead.
public class DatabaseHelper {

    private Connection connection;

    private static class Holder {
        static final DatabaseHelper INSTANCE = new DatabaseHelper();
    }

    private DatabaseHelper() {
        // FIX #2: DB.connect() now throws RuntimeException on failure instead of returning null,
        // so a failed connection causes a clear startup crash rather than a confusing NPE later.
        this.connection = DB.connect();
        DB.initDatabase(this.connection);

        // FIX #13: Register a JVM shutdown hook to cleanly close the connection when the app exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }

    public static DatabaseHelper getInstance() {
        return Holder.INSTANCE;
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
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void savePin(String pin) {
        String hashedPin = hashPin(pin);
        try {
            if (isPinSetup()) {
                String sql = "UPDATE users SET pin_hash = ? WHERE id = 1";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, hashedPin);
                    pstmt.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO users (pin_hash) VALUES (?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
                return hashedInput.equals(rs.getString("pin_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertExpense(Expense expense, int activeCycleId, String title) {
        String sql = "INSERT INTO expenses(title, category, amount, date, cycle_id) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, expense.getCategoryId());        // FIX #15: setInt, not setString
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDate().toString()); // FIX #17: LocalDate.toString()
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
            pstmt.setString(2, expense.getDate().toString()); // FIX #17: LocalDate.toString()
            pstmt.setInt(3, expense.getCategoryId());          // FIX #15: setInt
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

        // FIX #3: ResultSet is now in its own try-with-resources to guarantee it is closed.
        // Previously it was declared outside the try block and never explicitly closed.
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cycleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double amount = rs.getDouble("amount");
                    int catId = rs.getInt("category");                     // FIX #15: getInt
                    LocalDate date = LocalDate.parse(rs.getString("date")); // FIX #17: LocalDate

                    Expense expense = new Expense(cycleId, amount, date, catId);
                    expense.setId(rs.getInt("id"));
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    /**
     * FIX #16: Returns boolean so callers know if the save succeeded.
     * Previously a silent INSERT failure would cause SetupUI to call getCycle()
     * and get back the old active cycle with no error shown to the user.
     */
    public boolean saveCycle(BudgetCycle cycle) {
        if (cycle.getId() > 0) {
            String sql = "UPDATE budget_cycle SET remaining_balance=?, daily_limit=?, last_update=? WHERE id=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setDouble(1, cycle.getRemainingBalance());
                pstmt.setDouble(2, cycle.calculateDailyLimit());
                pstmt.setString(3, cycle.getLastUpdate() != null
                        ? cycle.getLastUpdate().toString()
                        : LocalDate.now().toString());
                pstmt.setInt(4, cycle.getId());
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Deactivate all existing cycles first
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("UPDATE budget_cycle SET active = 0");
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            String insertSql = "INSERT INTO budget_cycle(total_allowance, start_date, end_date, remaining_balance, daily_limit, last_update, active) VALUES(?, ?, ?, ?, ?, ?, 1)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                pstmt.setDouble(1, cycle.getTotalAllowance());
                pstmt.setString(2, cycle.getStartDate().toString());
                pstmt.setString(3, cycle.getEndDate().toString());
                pstmt.setDouble(4, cycle.getRemainingBalance());
                pstmt.setDouble(5, cycle.calculateDailyLimit());
                pstmt.setString(6, cycle.getStartDate().toString());
                pstmt.executeUpdate();
                System.out.println("Cycle successfully saved to database!");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public BudgetCycle getCycle() {
        String sql = "SELECT * FROM budget_cycle WHERE active = 1 ORDER BY id DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int id = rs.getInt("id");
                double totalAllowance = rs.getDouble("total_allowance");
                LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
                double dbRemaining = rs.getDouble("remaining_balance");

                String lastUpdateStr = rs.getString("last_update");
                LocalDate lastUpdate = (lastUpdateStr != null && !lastUpdateStr.isEmpty())
                        ? LocalDate.parse(lastUpdateStr)
                        : startDate;

                // FIX #17: Use factory method — never resets remainingBalance to totalAllowance
                return BudgetCycle.fromDatabase(id, totalAllowance, startDate, endDate, dbRemaining, lastUpdate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPrivacyEnabled(boolean isEnabled) {
        String sql = "UPDATE users SET privacy_enabled = ? WHERE id = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, isEnabled ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPrivacyEnabled() {
        String sql = "SELECT privacy_enabled FROM users LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("privacy_enabled") == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM expenses");
            stmt.executeUpdate("DELETE FROM budget_cycle");
            stmt.executeUpdate("DELETE FROM users");
            System.out.println("Database wiped successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}