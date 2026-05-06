package com.example.masroofy_app.model;

/**
 * Represents a category for expenses.
 * Each category has a unique id and a descriptive name.
 */
public class Category {
    private int id;
    private String name;

    /**
     * Constructs a new Category instance.
     *
     * @param id unique identifier of the category
     * @param name name of the category (e.g., Food, Transport)
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the category id.
     *
     * @return category id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the category name.
     *
     * @return category name
     */
    public String getName() {
        return name;
    }
}