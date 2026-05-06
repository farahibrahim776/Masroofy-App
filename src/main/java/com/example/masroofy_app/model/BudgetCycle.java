package com.example.masroofy_app.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a budget cycle for a user.
 * It manages total allowance, remaining balance, start/end dates,
 * and provides calculations for daily spending limits and validity checks.
 */
public class BudgetCycle {
    private int id;
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private double remainingBalance;
    private LocalDate lastUpdate;
    /**
     * Constructs a new BudgetCycle instance with initial values.
     *
     * @param id the unique identifier of the budget cycle
     * @param totalAllowance the total budget allocated for the cycle
     * @param startDate the start date of the cycle
     * @param endDate the end date of the cycle
     */
    public BudgetCycle(int id, double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingBalance = totalAllowance;
        this.lastUpdate = startDate;
    }

    /**
     * Creates a BudgetCycle object from database stored values.
     *
     * @param id the cycle id
     * @param totalAllowance total allowed budget
     * @param startDate start date
     * @param endDate end date
     * @param remainingBalance current remaining balance
     * @param lastUpdate last update date
     * @return a reconstructed BudgetCycle object
     */
    public static BudgetCycle fromDatabase(int id, double totalAllowance, LocalDate startDate,
                                           LocalDate endDate, double remainingBalance, LocalDate lastUpdate) {
        BudgetCycle cycle = new BudgetCycle(id, totalAllowance, startDate, endDate);
        cycle.remainingBalance = remainingBalance;
        cycle.lastUpdate = (lastUpdate != null) ? lastUpdate : startDate;
        return cycle;
    }
    /** @return cycle id */
    public int getId() { return id; }

    /** @return remaining balance in the cycle */
    public double getRemainingBalance() { return remainingBalance; }

    /** @return total allowed budget */
    public double getTotalAllowance() { return totalAllowance; }

    /** @return end date of the cycle */
    public LocalDate getEndDate() { return endDate; }

    /** @return start date of the cycle */
    public LocalDate getStartDate() { return startDate; }

    /** @return last update date */
    public LocalDate getLastUpdate() { return lastUpdate; }

    /**
     * Updates last update date.
     *
     * @param date new last update date
     */
    public void setLastUpdate(LocalDate date) { this.lastUpdate = date; }

    /**
     * Updates remaining balance.
     *
     * @param balance new balance value
     */
    public void setRemainingBalance(double balance) { this.remainingBalance = balance; }

    /**
     * Calculates the safe daily spending limit based on remaining balance and days left.
     *
     * @return daily limit value rounded to 2 decimal places
     */
    public double calculateDailyLimit() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (daysLeft <= 0) return 0.0;
        double raw = remainingBalance / daysLeft;
        return Math.round(raw * 100) / 100.0;
    }

    /**
     * Deducts spent amount from remaining balance and updates last update date.
     *
     * @param amountSpent amount spent from budget
     */
    public void updateRemainingBalance(double amountSpent) {
        this.remainingBalance -= amountSpent;
        this.lastUpdate = LocalDate.now();
    }

    /**
     * Checks whether the budget cycle is currently active.
     *
     * @return true if current date is within cycle range, otherwise false
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate));
    }

    /**
     * Gets remaining days until cycle ends.
     *
     * @return number of remaining days (minimum 0)
     */
    public long getRemainingDays() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return Math.max(days, 0);
    }
}
