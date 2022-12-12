module com.pklatka.evolutiongenerator {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.evolutiongenerator.controller;
    exports com.evolutiongenerator.stage;
    exports com.evolutiongenerator.utils;
    exports com.evolutiongenerator.handler;
    opens com.evolutiongenerator.controller to javafx.fxml;
    exports com.evolutiongenerator.application;
    opens com.evolutiongenerator.application to javafx.fxml;
    exports com.evolutiongenerator.model.map;
    exports com.evolutiongenerator.model.mapObject;
    exports com.evolutiongenerator.model.engine;
    exports com.evolutiongenerator.model.ui;
}