package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PinLoginController {

    @FXML
    private TextField pinField;

    @FXML
    public void unlock(ActionEvent event) {

        String pin = pinField.getText();

        if (pin == null || pin.trim().isEmpty()) {
            System.out.println("Please enter a PIN.");
            return;
        }

        boolean isCorrect = DatabaseHelper.getInstance().verifyPin(pin);

        if (isCorrect) {
            System.out.println("Login successful!");

            SceneManager.switchScene("/view/DashboardUI.fxml");

        } else {
            System.out.println("Incorrect PIN. Access Denied.");
            pinField.clear();
        }
    }
}