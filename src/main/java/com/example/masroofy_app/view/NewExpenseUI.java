package com.example.masroofy_app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class NewExpenseUI {
    private int selectedCategoryId = 6;

    @FXML
    private VBox rootBox;

    @FXML
    private Label amountLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button foodBtn;

    @FXML
    private Button transportBtn;

    @FXML
    private Button shoppingBtn;

    @FXML
    private Button otherBtn;

    @FXML
    private Button addCategoryBtn;

    @FXML
    public void initialize() {

        rootBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {

                newScene.widthProperty().addListener((o, oldVal, newVal) -> scaleUI(newScene));
                newScene.heightProperty().addListener((o, oldVal, newVal) -> scaleUI(newScene));
            }
        });

        cancelBtn.setOnAction(e -> System.out.println("Cancelled"));

        confirmBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountLabel.getText());
                com.example.masroofy_app.model.BudgetCycle activeCycle = 
                    com.example.masroofy_app.model.DatabaseHelper.getInstance().getCycle();

                if (activeCycle != null) {
                    com.example.masroofy_app.service.ExpenseManager manager = 
                        new com.example.masroofy_app.service.ExpenseManager();
                    manager.addExpense(activeCycle, "New Expense", amount, selectedCategoryId);
                    System.out.println("Expense successfully saved!");

                    Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardUI.fxml"));
                    Stage stage = (Stage) confirmBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                }
            } catch (Exception ex) {
                System.out.println("Error saving or navigating: " + ex.getMessage());
            }
        });

        foodBtn.setOnAction(e -> selectedCategoryId = 1);
        transportBtn.setOnAction(e -> selectedCategoryId = 2);
        shoppingBtn.setOnAction(e -> selectedCategoryId = 3);
        otherBtn.setOnAction(e -> selectedCategoryId = 6);

        addCategoryBtn.setOnAction(e -> System.out.println("Add Category clicked"));

        cancelBtn.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardUI.fxml"));
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void scaleUI(Scene scene) {

        double baseWidth = 400;
        double baseHeight = 600;

        double scaleX = scene.getWidth() / baseWidth;
        double scaleY = scene.getHeight() / baseHeight;

        double scale = Math.min(scaleX, scaleY);

        double minScale = 0.8;
        if(scale < minScale){
            scale=minScale;
        }
        rootBox.setScaleX(scale);
        rootBox.setScaleY(scale);
    }
}
