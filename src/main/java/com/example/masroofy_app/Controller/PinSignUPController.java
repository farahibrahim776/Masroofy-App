package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PinSignUPController {

    @FXML
    private TextField pinField;

    @FXML
    public void unlock(ActionEvent event) {

        String pin = pinField.getText();

        if (pin == null || pin.trim().isEmpty()) {
            System.out.println("PIN cannot be empty!");
            return;
        }

        DatabaseHelper.getInstance().savePin(pin);
        System.out.println("PIN successfully created and saved!");

        SceneManager.switchScene("/view/SetupUI.fxml");
    }
}