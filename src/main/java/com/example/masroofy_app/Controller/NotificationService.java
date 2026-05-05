package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationService {

    // FIX #11: Track whether each alert has already been shown so we don't
    // spam the user with the same popup on every single expense after the threshold.
    // These flags reset when a new NotificationService instance is created (i.e. on app restart),
    // which is the correct behaviour — the user should be reminded once per session.
    private boolean warned80Percent = false;
    private boolean warnedExceeded = false;

    /**
     * Resets warning flags — call this when a new budget cycle starts,
     * or when the balance recovers above a threshold (e.g. after deleting an expense).
     */
    public void resetWarnings() {
        warned80Percent = false;
        warnedExceeded = false;
    }

    /**
     * Resets the exceeded flag only — useful when a deletion brings the balance
     * back above zero but still under 80%.
     */
    public void resetExceededWarning() {
        warnedExceeded = false;
    }

    public boolean check80Percent(BudgetCycle cycle) {
        if (cycle == null || cycle.getTotalAllowance() == 0) {
            return false;
        }
        double spentAmount = cycle.getTotalAllowance() - cycle.getRemainingBalance();
        double threshold = cycle.getTotalAllowance() * 0.80; // FIX #11: double literal, not float
        return spentAmount >= threshold;
    }

    /**
     * FIX #11: Only sends the 80% warning ONCE per session.
     * Returns true if the alert was actually shown (first time), false if already warned.
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
     * FIX #11: Only sends the exceeded alert ONCE per session.
     * Returns true if the alert was actually shown (first time), false if already warned.
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