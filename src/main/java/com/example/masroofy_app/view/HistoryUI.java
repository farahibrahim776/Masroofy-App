package com.example.masroofy_app.view;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.service.ExpenseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class HistoryUI {

    @FXML
    private VBox todayBox;

    @FXML
    private VBox yesterdayBox;

    private ExpenseManager manager = new ExpenseManager();

    @FXML
    public void initialize() {

        // Today
        manager.addExpense(-100, 1, java.time.LocalDateTime.now());
        manager.addExpense(-100, 2, java.time.LocalDateTime.now());
        manager.addExpense(-100, 3, java.time.LocalDateTime.now());

        // Yesterday
        manager.addExpense(-50, 1, java.time.LocalDateTime.now().minusDays(1));
        manager.addExpense(-50, 3, java.time.LocalDateTime.now().minusDays(1));

        for (Expense e : manager.getAllExpenses()) {

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

        // amount
        Label amount = new Label(formatAmount(e.getAmount()));
        amount.setStyle(getAmountStyle(e.getAmount()));

        // category
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
}