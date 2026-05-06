package com.example.masroofy_app.model;

import java.time.LocalDate;

/**
 * Represents an expense within a budget cycle.
 * Stores information about amount, date, and category.
 */
public class Expense {
    private int id;
    private int cycleId;
    private double amount;
    private LocalDate date; 
    private int categoryId;

    /**
     * Constructs a new Expense object.
     *
     * @param cycleId the id of the associated budget cycle
     * @param amount the amount of the expense
     * @param date the date of the expense
     * @param categoryId the category id of the expense
     */
    public Expense(int cycleId, double amount, LocalDate date, int categoryId) {
        this.cycleId = cycleId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    /** @return expense id */
    public int getId() { return id; }

    /**
     * Sets the expense id.
     *
     * @param id expense id
     */
    public void setId(int id) { this.id = id; }

    /** @return associated cycle id */
    public int getCycleId() { return cycleId; }

    /**
     * Sets the cycle id.
     *
     * @param cycleId new cycle id
     */
    public void setCycleId(int cycleId) { this.cycleId = cycleId; }

    /** @return expense amount */
    public double getAmount() { return amount; }

    /**
     * Updates the expense amount.
     *
     * @param amount new amount
     */
    public void setAmount(double amount) { this.amount = amount; }

    /** @return expense date */
    public LocalDate getDate() { return date; }

    /** @return category id */
    public int getCategoryId() { return categoryId; }

    /**
     * Sets the category id.
     *
     * @param categoryId new category id
     */
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    /**
     * Updates both amount and category of the expense.
     *
     * @param amount new amount
     * @param categoryId new category id
     */
    public void update(double amount, int categoryId) {
        this.amount = amount;
        this.categoryId = categoryId;
    }

    /**
     * Returns the formatted date as a string.
     *
     * @return date in string format
     */
    public String getFormattedDate() {
        return date.toString(); 
    }
}
