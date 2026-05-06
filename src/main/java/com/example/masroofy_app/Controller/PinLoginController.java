package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

/**
 * Controller class responsible for handling PIN login authentication.
 * Validates user input and allows access to the dashboard if PIN is correct.
 */
public class PinLoginController {

    @FXML
    private PasswordField pinField;

    @FXML
    private Label errorLabel;

    /**
     * Handles the unlock/login action.
     * Validates the entered PIN against the database and navigates
     * to the dashboard if authentication is successful.
     *
     * @param event the action event triggered by the login button
     */
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


    /**
     * Displays an error message to the user or console if UI label is unavailable.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            System.out.println(message);
        }
    }
}