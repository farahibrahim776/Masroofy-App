package com.example.masroofy_app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsUI {

    @FXML
    private javafx.scene.control.CheckBox privacyCheck;

    @FXML
    public void goBack() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/HistoryUI.fxml")
            );

            Stage stage = (Stage) privacyCheck.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}