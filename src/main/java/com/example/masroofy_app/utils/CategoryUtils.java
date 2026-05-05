package com.example.masroofy_app.utils;

import java.util.HashMap;
import java.util.Map;

public class CategoryUtils {
    
    private static final Map<Integer, String> CATEGORY_MAP = new HashMap<>();

    static {
        CATEGORY_MAP.put(1, "Food");
        CATEGORY_MAP.put(2, "Transport");
        CATEGORY_MAP.put(3, "Shopping");
        CATEGORY_MAP.put(4, "Bills");
        CATEGORY_MAP.put(5, "Entertainment");
        CATEGORY_MAP.put(6, "Other");
    }

    public static String getCategoryName(int id) {
        return CATEGORY_MAP.getOrDefault(id, "Other");
    }
}