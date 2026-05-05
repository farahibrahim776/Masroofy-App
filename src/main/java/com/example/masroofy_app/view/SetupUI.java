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
    private Label errorLabel; // Add fx:id="errorLabel" in SetupUI.fxml to show errors inline

    private BudgetCycle cycle;

    // FIX #13: Use double instead of float for monetary values — no silent precision loss
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
            // FIX #13: Parse as double, not float
            double total = Double.parseDouble(amountField.getText());
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();

            cycle = inputCycleData(total, start, end);

            if (cycle == null) {
                showError("Invalid input. Check that the amount is positive and end date is after start date.");
                return;
            }

            // FIX #16: Check if saveCycle() succeeded before navigating away.
            // Previously a silent DB failure would let the app navigate to the dashboard
            // showing the old (or no) cycle, with no indication anything went wrong.
            boolean saved = DatabaseHelper.getInstance().saveCycle(cycle);
            if (!saved) {
                showError("Failed to save your budget cycle. Please try again.");
                return;
            }

            // Reload the saved cycle so we have the correct DB-assigned id
            cycle = DatabaseHelper.getInstance().getCycle();

            // FIX #16: If getCycle() returns null after a successful save, something is wrong
            if (cycle == null) {
                showError("Budget cycle saved but could not be loaded. Please restart the app.");
                return;
            }

            System.out.println("Cycle Created!");
            // FIX #14: Use SceneManager for consistent navigation
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