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
        // FIX #19: Guard against null stage with a clear error message
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
            // FIX #19: Previously exceptions were silently swallowed — the app stayed on the
            // current screen with no feedback. Now we show a clear error dialog so the user
            // (and developer) know a navigation failure occurred.
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