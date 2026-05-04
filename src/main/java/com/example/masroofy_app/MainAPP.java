package com.example.masroofy_app;

import com.example.masroofy_app.navigation.SceneManager;
import com.example.masroofy_app.model.DatabaseHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainAPP extends Application {

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

    public static void main(String[] args) {
        launch();
    }
}