package com.example.masroofy_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Paths;

public class DB {

    // FIX #18: Use a fixed path in the user's home directory
    private static final String URL = "jdbc:sqlite:" +
            Paths.get(System.getProperty("user.home"), ".masroofy", "masroofy.db").toString();

    public static Connection connect() {
        // FIX #2: Throw RuntimeException instead of returning null silently.
        // A null connection causes a NullPointerException on every SQL call,
        // which is much harder to debug than a clear startup failure here.
        try {
            java.io.File dbDir = Paths.get(System.getProperty("user.home"), ".masroofy").toFile();
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            Connection conn = DriverManager.getConnection(URL);
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            return conn;

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database at: " + URL, e);
        }
    }

    public static void initDatabase(Connection conn) {
        // FIX #2: Guard against null connection (defensive, since connect() now throws)
        if (conn == null) {
            throw new IllegalArgumentException("Cannot initialize database: connection is null.");
        }

        try (Statement stmt = conn.createStatement()) {

            // USERS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pin_hash TEXT,
                    privacy_enabled INTEGER DEFAULT 0
                );
            """);

            // BUDGET CYCLE
            // FIX #7: last_update column persists lastUpdate correctly
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS budget_cycle (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    total_allowance REAL NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    remaining_balance REAL NOT NULL,
                    daily_limit REAL NOT NULL,
                    last_update TEXT,
                    active INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
            """);

            // Migration: add last_update to existing databases that don't have it
            try {
                stmt.execute("ALTER TABLE budget_cycle ADD COLUMN last_update TEXT");
            } catch (Exception ignored) {
                // Column already exists — safe to ignore
            }

            // EXPENSES
            // FIX #15: category is INTEGER for proper type safety
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS expenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    category INTEGER NOT NULL,
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
            throw new RuntimeException("Failed to initialize database schema.", e);
        }
    }
}