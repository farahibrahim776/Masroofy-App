package com.example.masroofy_app.view;

import com.example.masroofy_app.model.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardUI.fxml"));
            Stage stage = (Stage) privacyCheck.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
