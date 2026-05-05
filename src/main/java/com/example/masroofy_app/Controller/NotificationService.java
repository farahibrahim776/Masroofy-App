package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationService {

    private boolean warned80Percent = false;
    private boolean warnedExceeded = false;

    public void resetWarnings() {
        warned80Percent = false;
        warnedExceeded = false;
    }

    public void resetExceededWarning() {
        warnedExceeded = false;
    }

    public boolean check80Percent(BudgetCycle cycle) {
        if (cycle == null || cycle.getTotalAllowance() == 0) {
            return false;
        }
        double spentAmount = cycle.getTotalAllowance() - cycle.getRemainingBalance();
        double threshold = cycle.getTotalAllowance() * 0.80; 
        return spentAmount >= threshold;
    }

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
