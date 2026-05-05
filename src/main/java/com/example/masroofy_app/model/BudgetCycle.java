package com.example.masroofy_app.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BudgetCycle {
    private int id;
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private double remainingBalance;
    private LocalDate lastUpdate;

    // FIX #17: Primary constructor for new cycles
    public BudgetCycle(int id, double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingBalance = totalAllowance;
        this.lastUpdate = startDate;
    }

    // FIX #17: Factory method for reconstructing from DB — does NOT reset remainingBalance
    public static BudgetCycle fromDatabase(int id, double totalAllowance, LocalDate startDate,
                                           LocalDate endDate, double remainingBalance, LocalDate lastUpdate) {
        BudgetCycle cycle = new BudgetCycle(id, totalAllowance, startDate, endDate);
        cycle.remainingBalance = remainingBalance;
        // FIX #4: If lastUpdate is null coming from DB, default to startDate (never null),
        // so checkNewDay() won't treat every expense as a new-day event on migrated databases.
        cycle.lastUpdate = (lastUpdate != null) ? lastUpdate : startDate;
        return cycle;
    }

    public int getId() { return id; }
    public double getRemainingBalance() { return remainingBalance; }
    public double getTotalAllowance() { return totalAllowance; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getLastUpdate() { return lastUpdate; }

    public void setLastUpdate(LocalDate date) { this.lastUpdate = date; }
    public void setRemainingBalance(double balance) { this.remainingBalance = balance; }

    /**
     * FIX #7: Single authoritative daily-limit calculation used by both
     * BudgetCycle and DashboardManager.getDailyLimit().
     * Rounds to 2 decimal places to match what is shown in the UI,
     * so the displayed value and the stored value are always identical.
     */
    public double calculateDailyLimit() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (daysLeft <= 0) return 0.0;
        double raw = remainingBalance / daysLeft;
        // Round to 2 decimal places — same precision used in DashboardManager previously
        return Math.round(raw * 100) / 100.0;
    }

    public void updateRemainingBalance(double amountSpent) {
        this.remainingBalance -= amountSpent;
        this.lastUpdate = LocalDate.now();
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate));
    }

    public long getRemainingDays() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return Math.max(days, 0);
    }
}