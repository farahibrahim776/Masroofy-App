package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

/**
 * Controller class responsible for handling PIN registration (sign up).
 * Validates PIN input, confirms matching PINs, and saves it to the database.
 */
public class PinSignUPController {

    @FXML private PasswordField pinField;
    @FXML private PasswordField confirmPinField;
    @FXML private Label errorLabel;

    private static final int MIN_PIN_LENGTH = 4;
    private static final int MAX_PIN_LENGTH = 8;

    /**
     * Handles the PIN registration process.
     * Validates user input, checks PIN rules, confirms matching PINs,
     * and saves the PIN to the database if valid.
     *
     * @param event the action event triggered by the register button
     */
    @FXML
    public void register(ActionEvent event) {

        String pin = pinField.getText();
        String confirmPin = confirmPinField != null ? confirmPinField.getText() : pin;

        if (pin == null || pin.trim().isEmpty()) {
            showError("PIN cannot be empty.");
            return;
        }

        if (pin.length() < MIN_PIN_LENGTH || pin.length() > MAX_PIN_LENGTH) {
            showError("PIN must be " + MIN_PIN_LENGTH + "–" + MAX_PIN_LENGTH + " digits.");
            return;
        }

        if (!pin.matches("\\d+")) {
            showError("PIN must contain numbers only.");
            return;
        }

        if (!pin.equals(confirmPin)) {
            showError("PINs do not match. Please try again.");
            pinField.clear();
            if (confirmPinField != null) confirmPinField.clear();
            return;
        }

        DatabaseHelper.getInstance().savePin(pin);
        System.out.println("PIN successfully created and saved!");

        SceneManager.switchScene("/view/SetupUI.fxml");
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
            System.out.println("PIN error: " + message);
        }
    }
}
