module com.example.masroofy_app {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // FXML loading
    opens com.example.masroofy_app.view to javafx.fxml, javafx.graphics;

    // Controllers
    opens com.example.masroofy_app.Controller to javafx.fxml;

    // Models
    opens com.example.masroofy_app.model to javafx.base;

    exports com.example.masroofy_app;
}