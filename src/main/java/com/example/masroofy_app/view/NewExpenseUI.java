package com.example.masroofy_app.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

public class NewExpenseUI {

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

        confirmBtn.setOnAction(e ->
                System.out.println("Confirmed: " + amountLabel.getText())
        );

        foodBtn.setOnAction(e -> System.out.println("Food selected"));

        transportBtn.setOnAction(e -> System.out.println("Transport selected"));

        shoppingBtn.setOnAction(e -> System.out.println("Shopping selected"));

        otherBtn.setOnAction(e -> System.out.println("Other selected"));

        addCategoryBtn.setOnAction(e -> System.out.println("Add Category clicked"));
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