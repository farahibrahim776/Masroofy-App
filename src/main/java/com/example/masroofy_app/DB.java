package com.example.masroofy_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DB {

    private static final String URL = "jdbc:sqlite:masroofy.db";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);

            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void initDatabase() {

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // USERS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pin_hash TEXT,
                    privacy_enabled INTEGER DEFAULT 0
                );
            """);

            // BUDGET CYCLE
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS budget_cycle (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    total_allowance REAL NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    remaining_balance REAL NOT NULL,
                    daily_limit REAL NOT NULL,
                    active INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
            """);

            // EXPENSES
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS expenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    category TEXT NOT NULL,
                    amount REAL NOT NULL,
                    date TEXT NOT NULL,
                    cycle_id INTEGER,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (cycle_id) REFERENCES budget_cycle(id)
                    ON DELETE CASCADE
                );
            """);

            // CATEGORIES
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE NOT NULL
                );
            """);

            // DEFAULT CATEGORIES
            stmt.executeUpdate("""
                INSERT OR IGNORE INTO categories(name) VALUES
                ('Food'),
                ('Transport'),
                ('Shopping'),
                ('Bills'),
                ('Entertainment'),
                ('Other');
            """);

            System.out.println("Database Ready.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}