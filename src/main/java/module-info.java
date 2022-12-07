module com.pklatka.evolutiongenerator {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.pklatka.evolutiongenerator.controller;
    exports com.pklatka.evolutiongenerator.stage;
    exports com.pklatka.evolutiongenerator.utils;
    exports com.pklatka.evolutiongenerator.handler;
    opens com.pklatka.evolutiongenerator.controller to javafx.fxml;
    exports com.pklatka.evolutiongenerator.application;
    opens com.pklatka.evolutiongenerator.application to javafx.fxml;
    exports com.pklatka.evolutiongenerator.model.map;
    exports com.pklatka.evolutiongenerator.model.mapObject;
    exports com.pklatka.evolutiongenerator.model.engine;
    exports com.pklatka.evolutiongenerator.model.ui;
}