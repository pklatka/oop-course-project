module com.pklatka.evolutiongenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pklatka.evolutiongenerator to javafx.fxml;
    exports com.pklatka.evolutiongenerator;
    exports com.pklatka.evolutiongenerator.controller;
    exports com.pklatka.evolutiongenerator.stage;
    exports com.pklatka.evolutiongenerator.utils;
    exports com.pklatka.evolutiongenerator.handler;
    exports com.pklatka.evolutiongenerator.model;
    opens com.pklatka.evolutiongenerator.controller to javafx.fxml;
}