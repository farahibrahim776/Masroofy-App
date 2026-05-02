package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import javafx.scene.control.Alert;

public class NotificationService {

    // US #6: Check if 80% of budget is consumed
    public boolean check80Percent(BudgetCycle cycle) {
        double spent = cycle.getTotalAllowance() - cycle.getRemainingBalance();
        double threshold = 0.80 * cycle.getTotalAllowance();
        return spent >= threshold;
    }

    public void sendWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Budget Threshold Alert");
        alert.setHeaderText("Warning: You have used 80% of your allowance.");
        alert.setContentText("Please review your spending to avoid running out of funds before the cycle ends.");
        alert.showAndWait();
    }
}