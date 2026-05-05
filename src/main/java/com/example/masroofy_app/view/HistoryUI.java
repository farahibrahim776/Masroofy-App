package com.example.masroofy_app.view;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.navigation.SceneManager;
import com.example.masroofy_app.service.ExpenseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class HistoryUI {

    @FXML private VBox todayBox;
    @FXML private VBox yesterdayBox;
    @FXML private VBox olderBox;  // Must be added to HistoryUI.fxml — see FIX #10 note below

    private ExpenseManager manager = new ExpenseManager();

    @FXML
    public void initialize() {
        todayBox.getChildren().clear();
        yesterdayBox.getChildren().clear();
        if (olderBox != null) olderBox.getChildren().clear();

        com.example.masroofy_app.model.BudgetCycle activeCycle =
                com.example.masroofy_app.model.DatabaseHelper.getInstance().getCycle();

        if (activeCycle == null) {
            System.out.println("No active cycle to show history for.");
            return;
        }

        java.util.List<Expense> realExpenses = manager.getAllExpenses(activeCycle.getId());

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // FIX #6 & #10: Properly bucket expenses into today / yesterday / older.
        // FIX #10 (HistoryUI): If olderBox is null (not yet in FXML), expenses older than
        // yesterday are NOT silently dumped into yesterdayBox anymore. Instead they are skipped
        // with a warning, so the UI never shows incorrect labels.
        // ACTION REQUIRED: Add a VBox with fx:id="olderBox" to HistoryUI.fxml so all history is shown.
        for (Expense e : realExpenses) {
            // FIX #17: Expense.date is now LocalDate — no more .toLocalDate() conversion needed
            LocalDate expenseDate = e.getDate();
            VBox item = createItem(e);

            if (expenseDate.equals(today)) {
                todayBox.getChildren().add(item);
            } else if (expenseDate.equals(yesterday)) {
                yesterdayBox.getChildren().add(item);
            } else if (olderBox != null) {
                olderBox.getChildren().add(item);
            } else {
                // FIX #10: Don't silently mislabel older expenses as "Yesterday".
                // Log a warning so the developer knows olderBox needs to be added to the FXML.
                System.out.println("WARNING: olderBox is null in HistoryUI.fxml — expense from "
                        + expenseDate + " is not displayed. Add fx:id='olderBox' to the FXML.");
            }
        }
    }

    private VBox createItem(Expense e) {
        HBox row = new HBox(20);

        Label amount = new Label(formatAmount(e.getAmount()));
        amount.setStyle(getAmountStyle(e.getAmount()));

        Label category = new Label(getCategoryName(e.getCategoryId()));

        Label date = new Label(e.getFormattedDate()); // FIX #17: getFormattedDate() now returns LocalDate.toString()
        date.setStyle("-fx-text-fill: #888; -fx-font-size: 11px;");

        row.getChildren().addAll(amount, category, date);

        VBox box = new VBox(row);
        box.setStyle("""
            -fx-background-color: white;
            -fx-padding: 10;
            -fx-border-color: #ddd;
            -fx-border-radius: 5;
            """);

        return box;
    }

    private String formatAmount(double amount) {
        return amount > 0 ? "+" + amount : "" + amount;
    }

    private String getAmountStyle(double amount) {
        return amount > 0
                ? "-fx-text-fill: green; -fx-font-weight: bold;"
                : "-fx-text-fill: red; -fx-font-weight: bold;";
    }

    private String getCategoryName(int id) {
        return com.example.masroofy_app.utils.CategoryUtils.getCategoryName(id);
    }

    // FIX #14: Use SceneManager for all navigation — consistent with the rest of the app
    @FXML
    private void goToDashboard(ActionEvent event) {
        SceneManager.switchScene("/view/DashboardUI.fxml");
    }

    @FXML
    private void goToStats(ActionEvent event) {
        SceneManager.switchScene("/view/StatsUI.fxml");
    }

    @FXML
    public void openSettings() {
        SceneManager.switchScene("/view/SettingsUI.fxml");
    }
}