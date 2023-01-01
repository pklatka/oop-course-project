package com.evolutiongenerator.stage;

import com.evolutiongenerator.application.Main;
import com.evolutiongenerator.controller.SimulationConfigurationController;
import com.evolutiongenerator.utils.FileChooser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Creates a new stage for the simulation configuration.
 *
 * @author Patryk Klatka
 */
public class SimulationConfigurationStage extends Application {

    /**
     * Starts the stage.
     *
     * @param stage Stage.
     * @throws IOException If the fxml file is not found.
     */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("simulation-configuration-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Set stage for controller
            SimulationConfigurationController controller = fxmlLoader.getController();
            controller.setFileChooserUtil(new FileChooser(stage));

            // Stage options
            stage.setTitle("Evolution Generator");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * Launches the stage.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
