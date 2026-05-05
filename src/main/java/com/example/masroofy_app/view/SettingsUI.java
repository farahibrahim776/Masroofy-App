package com.example.masroofy_app.view;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class SettingsUI {

    @FXML
    private CheckBox privacyCheck;

    @FXML
    public void initialize() {
        boolean isEnabled = DatabaseHelper.getInstance().isPrivacyEnabled();
        privacyCheck.setSelected(isEnabled);

        privacyCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            DatabaseHelper.getInstance().setPrivacyEnabled(newValue);
            System.out.println("Privacy setting saved: " + (newValue ? "ON" : "OFF"));
        });
    }

    @FXML
    public void goBack() {
        // FIX #1: Use SceneManager instead of manually loading FXML and setting a new scene.
        SceneManager.switchScene("/view/DashboardUI.fxml");
    }

    @FXML
    public void handleManageCategories() {
        // Placeholder — wire to a CategoryManager scene when implemented
        System.out.println("Manage Categories clicked");
    }

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

    @FXML
    public void handleChangePIN() {
        SceneManager.switchScene("/view/PinUISignup.fxml");
    }

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