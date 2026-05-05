package com.example.masroofy_app.view;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class SetupUI {

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Label errorLabel; 

    private BudgetCycle cycle;

    public BudgetCycle inputCycleData(double total, LocalDate start, LocalDate end) {
        if (!validateInput(total, start, end)) {
            return null;
        }
        return new BudgetCycle(0, total, start, end);
    }

    public boolean validateInput(double total, LocalDate start, LocalDate end) {
        return total > 0 && start != null && end != null && !end.isBefore(start);
    }

    @FXML
    public void handleStart() {
        try {
            double total = Double.parseDouble(amountField.getText());
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();

            cycle = inputCycleData(total, start, end);

            if (cycle == null) {
                showError("Invalid input. Check that the amount is positive and end date is after start date.");
                return;
            }

            boolean saved = DatabaseHelper.getInstance().saveCycle(cycle);
            if (!saved) {
                showError("Failed to save your budget cycle. Please try again.");
                return;
            }

            cycle = DatabaseHelper.getInstance().getCycle();

          
            if (cycle == null) {
                showError("Budget cycle saved but could not be loaded. Please restart the app.");
                return;
            }

            System.out.println("Cycle Created!");
            SceneManager.switchScene("/view/DashboardUI.fxml");

        } catch (NumberFormatException e) {
            showError("Please enter a valid number for the amount.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            System.out.println("Setup error: " + message);
        }
    }
}
