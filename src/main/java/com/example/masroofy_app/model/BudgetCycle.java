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

    public BudgetCycle(int id, double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingBalance = totalAllowance;
        this.lastUpdate = startDate;
    }

    public static BudgetCycle fromDatabase(int id, double totalAllowance, LocalDate startDate,
                                           LocalDate endDate, double remainingBalance, LocalDate lastUpdate) {
        BudgetCycle cycle = new BudgetCycle(id, totalAllowance, startDate, endDate);
        cycle.remainingBalance = remainingBalance;
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

    public double calculateDailyLimit() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (daysLeft <= 0) return 0.0;
        double raw = remainingBalance / daysLeft;
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
