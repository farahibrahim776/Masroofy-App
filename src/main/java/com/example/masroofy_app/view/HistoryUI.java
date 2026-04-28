package com.example.masroofy_app.view;

import com.example.masroofy_app.model.Expense;
import com.example.masroofy_app.service.ExpenseManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class HistoryUI {

    @FXML
    private TableView<Expense> table;

    @FXML
    private TableColumn<Expense, Double> colAmount;

    @FXML
    private TableColumn<Expense, String> colDate;

    private ExpenseManager manager = new ExpenseManager();

    @FXML
    public void initialize() {

        // test data
        manager.addExpense(100, 1);
        manager.addExpense(50, 2);

        // Amount column
        colAmount.setCellValueFactory(data ->
                new SimpleDoubleProperty(
                        data.getValue().getAmount()
                ).asObject()
        );

        // Date column
        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getDate().toLocalDate().toString()
                )
        );

        // fill table
        table.setItems(
                FXCollections.observableArrayList(
                        manager.getAllExpenses()
                )
        );
    }
}