package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PinLoginController {
    @FXML
    private TextField pinField;

    @FXML
    public void unlock(ActionEvent event) {
        String pin = pinField.getText();

        if (pin == null || pin.trim().isEmpty()) {
            System.out.println("Please enter a PIN.");
            return;
        }

        boolean isCorrect = DatabaseHelper.getInstance().verifyPin(pin);

        if (isCorrect) {
            System.out.println("Login successful!");
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardUI.fxml"));
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else {
            System.out.println("Incorrect PIN. Access Denied.");
            pinField.clear(); 
        }
    }
}
