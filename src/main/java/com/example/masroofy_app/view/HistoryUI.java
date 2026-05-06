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

/**
 * Controller class for the History screen.
 * Responsible for displaying user expenses grouped by date:
 * Today, Yesterday, and Older.
 */
public class HistoryUI {

    @FXML private VBox todayBox;
    @FXML private VBox yesterdayBox;
    @FXML private VBox olderBox;  

    private ExpenseManager manager = new ExpenseManager();

    /**
     * Initializes the History screen.
     * Loads expenses from the database and distributes them
     * into Today, Yesterday, and Older sections.
     */
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

        for (Expense e : realExpenses) {
            LocalDate expenseDate = e.getDate();
            VBox item = createItem(e);

            if (expenseDate.equals(today)) {
                todayBox.getChildren().add(item);
            } else if (expenseDate.equals(yesterday)) {
                yesterdayBox.getChildren().add(item);
            } else if (olderBox != null) {
                olderBox.getChildren().add(item);
            } else {
                System.out.println("WARNING: olderBox is null in HistoryUI.fxml — expense from "
                        + expenseDate + " is not displayed. Add fx:id='olderBox' to the FXML.");
            }
        }
    }

    /**
     * Creates a UI component representing a single expense.
     *
     * @param e expense object
     * @return VBox containing styled expense data
     */
    private VBox createItem(Expense e) {
        HBox row = new HBox(20);

        Label amount = new Label(formatAmount(e.getAmount()));
        amount.setStyle(getAmountStyle(e.getAmount()));

        Label category = new Label(getCategoryName(e.getCategoryId()));

        Label date = new Label(e.getFormattedDate()); 
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

    /**
     * Formats amount to include + or - sign.
     */
    private String formatAmount(double amount) {
        return amount > 0 ? "+" + amount : "" + amount;
    }

    /**
     * Returns CSS style based on amount type (income or expense).
     */
    private String getAmountStyle(double amount) {
        return amount > 0
                ? "-fx-text-fill: green; -fx-font-weight: bold;"
                : "-fx-text-fill: red; -fx-font-weight: bold;";
    }

    /**
     * Gets category name from category ID.
     */
    private String getCategoryName(int id) {
        return com.example.masroofy_app.utils.CategoryUtils.getCategoryName(id);
    }

    /**
     * Navigates to Dashboard screen.
     */
    @FXML
    private void goToDashboard(ActionEvent event) {
        SceneManager.switchScene("/view/DashboardUI.fxml");
    }

    /**
     * Navigates to Stats screen.
     */
    @FXML
    private void goToStats(ActionEvent event) {
        SceneManager.switchScene("/view/StatsUI.fxml");
    }

    /**
     * Opens Settings screen.
     */
    @FXML
    public void openSettings() {
        SceneManager.switchScene("/view/SettingsUI.fxml");
    }
}
