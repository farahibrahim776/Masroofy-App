module com.example.masroofy_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.masroofy_app to javafx.fxml;
    exports com.example.masroofy_app;
    exports com.example.masroofy_app.Controller;
    opens com.example.masroofy_app.Controller to javafx.fxml;
    exports service;
    opens service to javafx.fxml;
    exports view;
    opens view to javafx.fxml;
}