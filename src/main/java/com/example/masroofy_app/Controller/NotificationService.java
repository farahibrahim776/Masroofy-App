package com.example.masroofy_app.Controller;

import com.example.masroofy_app.model.BudgetCycle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationService {

    public boolean check80Percent(BudgetCycle cycle) {
        if (cycle == null || cycle.getTotalAllowance() == 0) {
            return false;
        }
        
        // Calculate spent amount
        double spentAmount = cycle.getTotalAllowance() - cycle.getRemainingBalance();
        double threshold = cycle.getTotalAllowance() * 0.80f;
        
        return spentAmount >= threshold;
    }

    public void sendWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Budget Warning");
        alert.setHeaderText("80% Limit Reached");
        alert.setContentText("Caution: You have used 80% or more of your total budget allowance. Please spend carefully!");
        alert.showAndWait();
    }

    public void sendExceededAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Budget Exceeded");
        alert.setHeaderText("Budget Exhausted");
        alert.setContentText("Warning: You have exceeded your total budget allowance for this cycle!");
        alert.showAndWait();
    }
}
