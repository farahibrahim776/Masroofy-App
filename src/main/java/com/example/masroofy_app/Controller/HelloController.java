package com.example.masroofy_app.Controller;
import com.example.masroofy_app.DB;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private TextField titleField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField dateField;

    @FXML
    private void onSaveClick() {

        String title = titleField.getText();
        double amount = Double.parseDouble(amountField.getText());
        String date = dateField.getText();

        String sql = "INSERT INTO expenses (title, amount, date) VALUES (?, ?, ?)";

        try {
            Connection conn = DB.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setDouble(2, amount);
            stmt.setString(3, date);

            stmt.executeUpdate();

            System.out.println("Expense saved!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}