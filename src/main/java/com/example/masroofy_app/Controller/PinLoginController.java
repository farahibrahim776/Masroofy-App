package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class PinLoginController {

    @FXML
    private PasswordField pinField;

    @FXML
    private Label errorLabel;

    @FXML
    public void unlock(ActionEvent event) {

        String pin = pinField.getText();

        if (pin == null || pin.trim().isEmpty()) {
            showError("Please enter your PIN.");
            return;
        }

        boolean isCorrect = DatabaseHelper.getInstance().verifyPin(pin);

        if (isCorrect) {
            System.out.println("Login successful!");
            SceneManager.switchScene("/view/DashboardUI.fxml");
        } else {
            showError("Incorrect PIN. Please try again.");
            pinField.clear();
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            System.out.println(message);
        }
    }
}