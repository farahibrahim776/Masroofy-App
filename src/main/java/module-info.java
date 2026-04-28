module com.example.masroofy_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.masroofy_app to javafx.fxml;
    exports com.example.masroofy_app;
}