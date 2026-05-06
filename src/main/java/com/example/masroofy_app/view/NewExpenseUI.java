package com.example.masroofy_app.view;

import com.example.masroofy_app.navigation.SceneManager;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Controller class for the Add New Expense screen.
 * Handles user input, validation, category selection,
 * and saving the expense to the database.
 */
public class NewExpenseUI {

    private int selectedCategoryId = 6; 

    @FXML private VBox rootBox;
    @FXML private TextField amountField;
    @FXML private TextField titleField; 
    @FXML private Label errorLabel;    
    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;
    @FXML private Button foodBtn;
    @FXML private Button transportBtn;
    @FXML private Button shoppingBtn;
    @FXML private Button billsBtn;         
    @FXML private Button entertainmentBtn;  
    @FXML private Button otherBtn;

    /**
     * Initializes the UI.
     * - Adds responsive scaling behavior
     * - Handles category selection
     * - Handles confirm and cancel actions
     */
@FXML
public void initialize() {

    rootBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            newScene.widthProperty().addListener((o, oldVal, newVal) -> scaleUI(newScene));
            newScene.heightProperty().addListener((o, oldVal, newVal) -> scaleUI(newScene));
        }
    });

    // Category buttons list
    java.util.List<Button> categoryBtns = java.util.List.of(
        foodBtn, transportBtn, shoppingBtn, billsBtn, entertainmentBtn, otherBtn
    );

    // Helper to highlight selected button
    java.util.function.Consumer<Button> selectCategory = (selected) -> {
        for (Button btn : categoryBtns) {
            btn.setStyle("-fx-background-color: #f1f2f6; -fx-text-fill: #2c3e50; -fx-background-radius: 20px; -fx-padding: 8px 15px; -fx-cursor: hand;");
        }
        selected.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 20px; -fx-padding: 8px 15px; -fx-cursor: hand; -fx-font-weight: bold;");
    };

    foodBtn.setOnAction(e -> { selectedCategoryId = 1; selectCategory.accept(foodBtn); });
    transportBtn.setOnAction(e -> { selectedCategoryId = 2; selectCategory.accept(transportBtn); });
    shoppingBtn.setOnAction(e -> { selectedCategoryId = 3; selectCategory.accept(shoppingBtn); });
    billsBtn.setOnAction(e -> { selectedCategoryId = 4; selectCategory.accept(billsBtn); });
    entertainmentBtn.setOnAction(e -> { selectedCategoryId = 5; selectCategory.accept(entertainmentBtn); });
    otherBtn.setOnAction(e -> { selectedCategoryId = 6; selectCategory.accept(otherBtn); });

    /**
     * Confirm button logic:
     * - Validate input
     * - Save expense
     * - Navigate to dashboard
     */
    confirmBtn.setOnAction(e -> {
        String amountText = amountField.getText();
        if (amountText == null || amountText.trim().isEmpty()) {
            showError("Please enter an amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }

        String title = (titleField != null && !titleField.getText().trim().isEmpty())
                ? titleField.getText().trim()
                : "Expense";

        com.example.masroofy_app.model.BudgetCycle activeCycle =
                com.example.masroofy_app.model.DatabaseHelper.getInstance().getCycle();

        if (activeCycle == null) {
            showError("No active budget cycle found. Please set up a budget first.");
            return;
        }

        com.example.masroofy_app.service.ExpenseManager manager =
                new com.example.masroofy_app.service.ExpenseManager();
        manager.addExpense(activeCycle, title, amount, selectedCategoryId);
        System.out.println("Expense successfully saved!");

        SceneManager.switchScene("/view/DashboardUI.fxml");
    });

    cancelBtn.setOnAction(e -> SceneManager.switchScene("/view/DashboardUI.fxml"));
}

    /**
     * Displays error message to the user.
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            System.out.println("Input error: " + message);
        }
    }

    /**
     * Scales UI responsively based on window size.
     */
    private void scaleUI(javafx.scene.Scene scene) {
        double baseWidth = 400;
        double baseHeight = 600;
        double scale = Math.min(scene.getWidth() / baseWidth, scene.getHeight() / baseHeight);
        double minScale = 0.8;
        if (scale < minScale) scale = minScale;
        rootBox.setScaleX(scale);
        rootBox.setScaleY(scale);
    }
}
