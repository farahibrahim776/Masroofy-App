package com.example.masroofy_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Paths;

/**
 * Database utility class responsible for creating and managing
 * the SQLite database connection and initializing database tables.
 */
public class DB {

    private static final String URL = "jdbc:sqlite:" +
            Paths.get(System.getProperty("user.home"), ".masroofy", "masroofy.db").toString();

    /**
     * Establishes and returns a connection to the SQLite database.
     * Also ensures that the database directory exists and enables foreign keys.
     *
     * @return active database Connection
     * @throws RuntimeException if the connection fails
     */
    public static Connection connect() {
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

    /**
     * Initializes the database by creating required tables if they do not exist.
     * Includes users, budget_cycle, and expenses tables.
     *
     * @param conn active database connection (must not be null)
     * @throws IllegalArgumentException if connection is null
     */
    public static void initDatabase(Connection conn) {
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

            try {
                stmt.execute("ALTER TABLE budget_cycle ADD COLUMN last_update TEXT");
            } catch (Exception ignored) {
                // Column already exists — safe to ignore
            }

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
