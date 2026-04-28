package com.example.masroofy_app.model;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private double amount;
    private LocalDateTime date;
    private int categoryId;

    public Expense(int id, double amount, LocalDateTime date, int categoryId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static Expense create(int id, double amount, int categoryId) {
        return new Expense(id, amount, LocalDateTime.now(), categoryId);
    }

    public void update(double amount, int categoryId) {
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public void delete() {
    }
}