package com.example.masroofy_app.view;

import com.example.masroofy_app.model.BudgetCycle;
import com.example.masroofy_app.model.DatabaseHelper;
import com.example.masroofy_app.navigation.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * Controller class for the Setup screen.
 * Responsible for creating and validating a new budget cycle,
 * handling user input, and navigating to the dashboard after setup.
 */
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

    /**
     * Creates a BudgetCycle object from user input after validation.
     *
     * @param total the total budget amount entered by the user
     * @param start the start date of the budget cycle
     * @param end the end date of the budget cycle
     * @return a valid BudgetCycle object if input is valid, otherwise null
     */
    public BudgetCycle inputCycleData(double total, LocalDate start, LocalDate end) {
        if (!validateInput(total, start, end)) {
            return null;
        }
        return new BudgetCycle(0, total, start, end);
    }

    /**
     * Validates user input for the budget cycle.
     *
     * @param total the total budget amount
     * @param start the start date
     * @param end the end date
     * @return true if input is valid, false otherwise
     */
    public boolean validateInput(double total, LocalDate start, LocalDate end) {
        return total > 0 && start != null && end != null && !end.isBefore(start);
    }

    /**
     * Handles the start button action.
     * Reads user input, validates it, saves the budget cycle to the database,
     * and navigates to the Dashboard screen if successful.
     * Displays error messages if validation or saving fails.
     */
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

    /**
     * Displays an error message on the UI or console if label is not available.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            System.out.println("Setup error: " + message);
        }
    }
}
