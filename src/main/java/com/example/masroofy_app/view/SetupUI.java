package com.example.masroofy_app.view;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.service.ExpenseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SetupUI {

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    private BudgetCycle cycle;

    private ExpenseManager manager = new ExpenseManager();

    // create cycle
    public BudgetCycle inputCycleData(float total, LocalDate start, LocalDate end) {

        if (!validateInput(total, start, end)) {
            return null;
        }

        return new BudgetCycle(1, total, start, end);
    }

    // validate
    public boolean validateInput(float total, LocalDate start, LocalDate end) {
        return total > 0 && start != null && end != null && !end.isBefore(start);
    }

    @FXML
    public void handleStart() {

        try {
            float total = Float.parseFloat(amountField.getText());
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();

            cycle = inputCycleData(total, start, end);

            if (cycle == null) {
                System.out.println("Invalid Input");
                return;
            }

            System.out.println("Cycle Created!");


            // Navigation
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/HistoryUI.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) amountField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}