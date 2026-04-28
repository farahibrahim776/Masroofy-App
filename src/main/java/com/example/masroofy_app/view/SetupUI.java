package com.example.masroofy_app.view;

import com.example.masroofy_app.model.BudgetCycle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SetupUI {

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    private BudgetCycle cycle;

    @FXML
    public void handleStart() throws Exception {

        double amount = Double.parseDouble(amountField.getText());
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (amount <= 0 || start == null || end == null || end.isBefore(start)) {
            System.out.println("Invalid input");
            return;
        }

        cycle = new BudgetCycle(1, amount, start, end);

        System.out.println("Cycle Created!");

        // Navigate to History
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/HistoryUI.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.setScene(scene);
    }
}