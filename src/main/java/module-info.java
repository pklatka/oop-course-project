module com.pklatka.evolutiongenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pklatka.evolutiongenerator to javafx.fxml;
    exports com.pklatka.evolutiongenerator;
    exports com.pklatka.evolutiongenerator.controller;
    opens com.pklatka.evolutiongenerator.controller to javafx.fxml;
}