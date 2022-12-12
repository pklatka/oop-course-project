package com.evolutiongenerator.stage;

import com.evolutiongenerator.application.Main;
import com.evolutiongenerator.controller.SimulationConfigurationController;
import com.evolutiongenerator.utils.FileChooser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationConfigurationStage extends Application {

    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("start-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Set stage for controller
            SimulationConfigurationController controller = (SimulationConfigurationController)fxmlLoader.getController();
            controller.setFileChooserUtil(new FileChooser(stage));

            // Stage options
            stage.setTitle("Evolution Generator");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
