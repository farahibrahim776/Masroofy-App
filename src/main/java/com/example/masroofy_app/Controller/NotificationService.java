package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Service class responsible for handling budget-related notifications.
 * It triggers alerts when the user reaches 80% of their budget
 * or exceeds the total budget limit.
 */
public class NotificationService {

    private boolean warned80Percent = false;
    private boolean warnedExceeded = false;

    /**
     * Resets all warning flags.
     * Used when starting a new budget cycle.
     */
    public void resetWarnings() {
        warned80Percent = false;
        warnedExceeded = false;
    }

    /**
     * Resets only the exceeded budget warning flag.
     * Allows re-triggering of exceeded alerts if needed.
     */
    public void resetExceededWarning() {
        warnedExceeded = false;
    }

    /**
     * Checks whether the user has reached 80% of the total budget allowance.
     *
     * @param cycle the current budget cycle
     * @return true if spent amount is greater than or equal to 80% of total budget
     */
    public boolean check80Percent(BudgetCycle cycle) {
        if (cycle == null || cycle.getTotalAllowance() == 0) {
            return false;
        }
        double spentAmount = cycle.getTotalAllowance() - cycle.getRemainingBalance();
        double threshold = cycle.getTotalAllowance() * 0.80; 
        return spentAmount >= threshold;
    }

    /**
     * Displays a warning alert when the user reaches 80% of their budget.
     * This alert is shown only once per cycle.
     *
     * @return true if the warning was shown, false if it was already triggered
     */
    public boolean sendWarning() {
        if (warned80Percent) return false;
        warned80Percent = true;

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Budget Warning");
            alert.setHeaderText("80% Limit Reached");
            alert.setContentText("Caution: You have used 80% or more of your total budget allowance. Please spend carefully!");
            alert.showAndWait();
        });
        return true;
    }

    /**
     * Displays an alert when the user exceeds their total budget.
     * This alert is shown only once per cycle.
     *
     * @return true if the alert was shown, false if it was already triggered
     */
    public boolean sendExceededAlert() {
        if (warnedExceeded) return false;
        warnedExceeded = true;

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Budget Exceeded");
            alert.setHeaderText("Budget Exhausted");
            alert.setContentText("Warning: You have exceeded your total budget allowance for this cycle!");
            alert.showAndWait();
        });
        return true;
    }
}
