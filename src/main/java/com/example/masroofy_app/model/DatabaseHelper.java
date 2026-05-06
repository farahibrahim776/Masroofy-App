package com.example.masroofy_app.model;

import com.example.masroofy_app.DB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class responsible for all database operations in the application.
 *
 * It manages a single shared database connection and initializes the database
 * on startup. It also ensures the connection is safely closed when the
 * application shuts down using a shutdown hook.
 */
public class DatabaseHelper {


    /**
     * Active database connection used for all SQL operations.
     */
    private Connection connection;

    /**
     * Holder class used to implement the Singleton pattern in a thread-safe way.
     */
    private static class Holder {
        static final DatabaseHelper INSTANCE = new DatabaseHelper();
    }

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the database connection and sets up the schema if needed.
     * Also registers a shutdown hook to close the connection safely.
     */
    private DatabaseHelper() {
        this.connection = DB.connect();
        DB.initDatabase(this.connection);

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

    /**
     * Returns the single instance of DatabaseHelper.
     *
     * @return the singleton instance
     */
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

    /**
     * Checks whether a PIN is already set in the database.
     *
     * @return true if a PIN exists, false otherwise
     */
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

    /**
     * Saves a user PIN in the database.
     *
     * If a PIN already exists, it updates the existing record.
     * Otherwise, it inserts a new record.
     *
     * The PIN is securely stored using SHA-256 hashing.
     *
     * @param pin the plain text PIN entered by the user
     */
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

    /**
     * Verifies whether the provided PIN matches the stored hashed PIN.
     *
     * The input PIN is hashed and compared with the stored value in the database.
     *
     * @param inputPin the PIN entered by the user
     * @return true if the PIN is correct, false otherwise
     */
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

    /**
     * Inserts a new expense record into the database.
     *
     * @param expense the expense object containing amount, category, and date
     * @param activeCycleId the ID of the currently active budget cycle
     * @param title the title/description of the expense
     */
    public void insertExpense(Expense expense, int activeCycleId, String title) {
        String sql = "INSERT INTO expenses(title, category, amount, date, cycle_id) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, expense.getCategoryId());        
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDate().toString()); 
            pstmt.setInt(5, activeCycleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing expense in the database.
     *
     * @param expense the expense object with updated values
     */
    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET amount = ?, date = ?, category = ? WHERE id = ?";
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

    /**
     * Deletes an expense from the database by its ID.
     *
     * @param expenseId the ID of the expense to delete
     */
    public void deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all expenses for a specific budget cycle.
     *
     * @param cycleId the ID of the budget cycle
     * @return a list of expenses belonging to that cycle
     */
    public List<Expense> getExpenses(int cycleId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE cycle_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cycleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double amount = rs.getDouble("amount");
                    int catId = rs.getInt("category");                     
                    LocalDate date = LocalDate.parse(rs.getString("date")); 

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
     * Saves a budget cycle to the database.
     *
     * If the cycle already exists (id > 0), it updates the existing record.
     * Otherwise, it deactivates all previous cycles and inserts a new active cycle.
     *
     * @param cycle the budget cycle object containing financial data
     * @return true if the operation was successful, false otherwise
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

    /**
     * Retrieves the currently active budget cycle from the database.
     *
     * The method returns the most recent active cycle and reconstructs it
     * into a BudgetCycle object.
     *
     * @return the active BudgetCycle, or null if none exists
     */
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

         
                return BudgetCycle.fromDatabase(id, totalAllowance, startDate, endDate, dbRemaining, lastUpdate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the privacy setting for the user.
     *
     * @param isEnabled true to enable privacy mode, false to disable it
     */
    public void setPrivacyEnabled(boolean isEnabled) {
        String sql = "UPDATE users SET privacy_enabled = ? WHERE id = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, isEnabled ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether privacy mode is enabled for the user.
     *
     * @return true if privacy is enabled, false otherwise
     */
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

    /**
     * Completely resets the database by deleting all data.
     *
     * This includes:
     * - All expenses
     * - All budget cycles
     * - All users
     *
     * @return true if reset was successful, false otherwise
     */
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
