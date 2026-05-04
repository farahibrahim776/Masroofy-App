package com.example.masroofy_app.view;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.service.ExpenseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HistoryUI {

    @FXML
    private VBox todayBox;

    @FXML
    private VBox yesterdayBox;

    private ExpenseManager manager = new ExpenseManager();

    @FXML
    public void initialize() {
        todayBox.getChildren().clear();
        yesterdayBox.getChildren().clear();

        com.example.masroofy_app.model.BudgetCycle activeCycle = com.example.masroofy_app.model.DatabaseHelper.getInstance().getCycle();

        if (activeCycle == null) {
            System.out.println("No active cycle to show history for.");
            return;
        }

        java.util.List<Expense> realExpenses = manager.getAllExpenses(activeCycle.getId());

        for (Expense e: realExpenses) {
            VBox item = createItem(e);

            if (e.getDate().toLocalDate().equals(java.time.LocalDate.now())) {
                todayBox.getChildren().add(item);
            } else {
                yesterdayBox.getChildren().add(item);
            }
        }
    }

    private VBox createItem(Expense e) {

        HBox row = new HBox(20);

        Label amount = new Label(formatAmount(e.getAmount()));
        amount.setStyle(getAmountStyle(e.getAmount()));

        Label category = new Label(getCategoryName(e.getCategoryId()));

        row.getChildren().addAll(amount, category);

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
        return switch (id) {
            case 1 -> "Food";
            case 2 -> "Entertainment";
            case 3 -> "Transportation";
            default -> "Other";
        };
    }

    // Navigation
    @FXML
    private void goToDashboard(javafx.event.ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/DashboardUI.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goToStats(javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/StatsUI.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openSettings() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/SettingsUI.fxml")
            );

            Stage stage = (Stage) todayBox.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
