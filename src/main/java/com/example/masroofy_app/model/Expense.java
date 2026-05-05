package com.example.masroofy_app.model;

import java.time.LocalDate;

/**
 * FIX #17: Changed date field from LocalDateTime to LocalDate.
 *
 * The previous code stored LocalDateTime but:
 *   - Only saved LocalDate.toString() to the DB (losing time)
 *   - Reconstructed it as d.atStartOfDay() (always midnight — time was meaningless)
 *
 * LocalDate is the correct type. If full timestamps are needed in future,
 * the DB schema and all related classes should be updated together consistently.
 */
public class Expense {
    private int id;
    private int cycleId;
    private double amount;
    private LocalDate date;       // FIX #17: was LocalDateTime, now LocalDate
    private int categoryId;

    public Expense(int cycleId, double amount, LocalDate date, int categoryId) {
        this.cycleId = cycleId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCycleId() { return cycleId; }
    public void setCycleId(int cycleId) { this.cycleId = cycleId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }  // FIX #17: returns LocalDate

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public void update(double amount, int categoryId) {
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public String getFormattedDate() {
        return date.toString();  // FIX #17: no longer needs .toLocalDate() conversion
    }
}