package com.example.masroofy_app.model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private int cycleId;
    private double amount;
    private LocalDate date; 
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

    public LocalDate getDate() { return date; }  

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public void update(double amount, int categoryId) {
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public String getFormattedDate() {
        return date.toString(); 
    }
}
