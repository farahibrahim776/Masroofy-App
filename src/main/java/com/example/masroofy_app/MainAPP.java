package com.example.masroofy_app;

import com.example.masroofy_app.navigation.SceneManager;
import com.example.masroofy_app.model.DatabaseHelper;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the Masroofy application.
 * Responsible for initializing the application,
 * setting up the stage, and routing the user
 * to the correct initial screen (Login or Signup).
 */
public class MainAPP extends Application {

    /**
     * Starts the JavaFX application.
     * Initializes SceneManager and determines
     * whether to show Login or Signup screen
     * based on whether a PIN is already set.
     *
     * @param stage the primary stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);

        if (DatabaseHelper.getInstance().isPinSetup()) {
            SceneManager.switchScene("/view/PinUILogin.fxml");
        } else {
            SceneManager.switchScene("/view/PinUISignup.fxml");
        }

        stage.setTitle("Masroofy App");
        stage.show();
    }

    /**
     * Main method - launches the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}