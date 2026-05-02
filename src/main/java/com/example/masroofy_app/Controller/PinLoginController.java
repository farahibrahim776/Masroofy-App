package com.example.masroofy_app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PinLoginController {
    @FXML
    private TextField pinField;

    @FXML
    public void unlock(ActionEvent event) {
        String pin = pinField.getText();
        System.out.println("Unlock clicked! PIN entered: " + pin);
    }
}
