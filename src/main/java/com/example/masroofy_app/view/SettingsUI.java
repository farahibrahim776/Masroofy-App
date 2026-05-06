package com.example.masroofy_app.view;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

/**
 * Controller class for the Settings screen.
 * Handles user settings such as privacy toggle, budget cycle reset,
 * database reset, and navigation between different screens.
 */
public class SettingsUI {

    @FXML
    private CheckBox privacyCheck;

    /**
     * Initializes the Settings screen.
     * Loads the current privacy setting from the database and binds it
     * to the checkbox. Also listens for changes to update the database
     * in real time.
     */
    @FXML
    public void initialize() {
        boolean isEnabled = DatabaseHelper.getInstance().isPrivacyEnabled();
        privacyCheck.setSelected(isEnabled);

        privacyCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            DatabaseHelper.getInstance().setPrivacyEnabled(newValue);
            System.out.println("Privacy setting saved: " + (newValue ? "ON" : "OFF"));
        });
    }

    /**
     * Navigates back to the Dashboard screen.
     */
    @FXML
    public void goBack() {
        SceneManager.switchScene("/view/DashboardUI.fxml");
    }

    /**
     * Handles the action of managing categories.
     * Currently prints a message to the console.
     */
    @FXML
    public void handleManageCategories() {
        System.out.println("Manage Categories clicked");
    }

    /**
     * Resets the current budget cycle after user confirmation.
     * Deactivates the existing cycle in the database and navigates
     * the user to the setup screen to create a new cycle.
     */
    @FXML
    public void handleResetCycle() {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset Cycle");
        confirm.setHeaderText("Reset the current budget cycle?");
        confirm.setContentText("This will deactivate the current cycle. You will need to set up a new one.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Deactivate cycle in DB and navigate to setup
                DatabaseHelper.getInstance().saveCycle(
                    new com.example.masroofy_app.model.BudgetCycle(
                        0, 0, java.time.LocalDate.now(), java.time.LocalDate.now()));
                SceneManager.switchScene("/view/SetupUI.fxml");
            }
        });
    }

    /**
     * Navigates the user to the PIN change (signup) screen.
     */
    @FXML
    public void handleChangePIN() {
        SceneManager.switchScene("/view/PinUISignup.fxml");
    }

    /**
     * Resets the entire database after user confirmation.
     * Deletes all stored data including expenses, budget cycles, and PIN.
     * If successful, redirects the user to the PIN setup screen.
     * Otherwise, shows an error message.
     */
    @FXML
    public void handleResetDatabase() {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset Database");
        confirm.setHeaderText("Delete ALL data?");
        confirm.setContentText("This will permanently erase all expenses, cycles, and your PIN. This cannot be undone.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                boolean success = DatabaseHelper.getInstance().resetDatabase();
                if (success) {
                    SceneManager.switchScene("/view/PinUISignup.fxml");
                } else {
                    javafx.scene.control.Alert error = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Reset Failed");
                    error.setContentText("Something went wrong. Please try again.");
                    error.showAndWait();
                }
            }
        });
    }
}
