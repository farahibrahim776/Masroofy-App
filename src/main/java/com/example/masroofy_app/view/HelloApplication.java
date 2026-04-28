package com.example.masroofy_app.view;

import java.sql.Connection;

import com.example.masroofy_app.DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Connection conn = DB.connect();
        DB.initDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource( "/com/example/masroofy_app/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}