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
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Map;

/**
 * Creates a new simulation stage
 *
 * @author Patryk Klatka
 */
public class SimulationStage extends Application {
    private final Map<ConfigurationConstant, ISimulationConfigurationValue> args;

    /**
     * SimulationStage constructor.
     *
     * @param args  Simulation configuration
     * @param stage Stage
     * @throws IOException If the fxml file is not found
     */
    public SimulationStage(Map<ConfigurationConstant, ISimulationConfigurationValue> args, Stage stage) throws IOException {
        this.args = args;
        start(stage);
    }

    /**
     * Starts simulation stage.
     *
     * @param primaryStage Stage
     * @throws IOException If the fxml file is not found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("simulation-window.fxml"));

        Parent root = fxmlLoader.load();

        // Set arguments for controller
        SimulationController controller = fxmlLoader.getController();
        controller.setSimulationOptions(args);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Evolution Generator");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, controller::exit);
    }

    /**
     * Launches the stage.
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
