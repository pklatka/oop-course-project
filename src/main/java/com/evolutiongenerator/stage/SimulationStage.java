package com.evolutiongenerator.stage;

import com.evolutiongenerator.application.Main;
import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.controller.SimulationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class SimulationStage extends Application {
    private Map<ConfigurationConstant, ISimulationConfigurationValue> args;

    public SimulationStage(Map<ConfigurationConstant, ISimulationConfigurationValue> args, Stage stage) throws IOException {
        this.args = args;
        start(stage);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("simulation-window.fxml"));

        Parent root = fxmlLoader.load();

        // Set arguments for controller
        SimulationController controller = fxmlLoader.getController();
        controller.setArgs(args);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Evolution Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
