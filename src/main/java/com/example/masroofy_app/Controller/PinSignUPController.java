package com.example.masroofy_app.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;;

public class PinSignUPController {
    @FXML
    private TextField pinField;

    @FXML
    public void unlock(ActionEvent event) {
        String pin = pinField.getText();
        System.out.println("Unlock clicked! PIN entered: " + pin);
    }
}
