package com.example.masroofy_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:masroofy.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void initDatabase() {
        try (Connection conn = connect()) {

            String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "amount REAL," +
                    "date TEXT" +
                    ");";

            conn.createStatement().execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}