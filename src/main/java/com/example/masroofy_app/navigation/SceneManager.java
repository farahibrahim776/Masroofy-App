package com.example.masroofy_app.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utility class responsible for managing scene navigation across the application.
 * It handles switching between FXML screens and error handling during navigation.
 */
public class SceneManager {

    private static Stage stage;

    /**
     * Initializes the SceneManager with the primary application stage.
     *
     * @param s the primary Stage of the application
     */
    public static void init(Stage s) {
        stage = s;
    }

    /**
     * Switches the current scene to a new FXML screen.
     * Loads the FXML file, sets it as the current scene, and displays it on the stage.
     * If loading fails, an error alert is shown.
     *
     * @param fxmlPath the path to the FXML file to load
     * @throws IllegalStateException if SceneManager.init() was not called before usage
     */
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
