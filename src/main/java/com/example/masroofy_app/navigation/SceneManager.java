package com.example.masroofy_app.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    public static void init(Stage s) {
        stage = s;
    }

    public static void switchScene(String fxmlPath) {
        if (stage == null) {
            throw new IllegalStateException(
                "SceneManager.init(stage) was never called. " +
                "Call SceneManager.init(primaryStage) in Application.start() before switching scenes."
            );
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Navigation Error");
                alert.setHeaderText("Failed to load screen");
                alert.setContentText("Could not load: " + fxmlPath + "\n\n" + e.getMessage());
                alert.showAndWait();
            });
        }
    }
}
