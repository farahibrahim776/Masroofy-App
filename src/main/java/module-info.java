module com.example.masroofy_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.masroofy_app to javafx.fxml;
    exports com.example.masroofy_app;
}