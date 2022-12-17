package com.evolutiongenerator.stage;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.model.engine.SimulationEngine;
import com.evolutiongenerator.model.map.AbstractWorldMap;
import com.evolutiongenerator.model.map.GrassField;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.MoveDirection;
import com.evolutiongenerator.model.ui.GuiElementBox;
import com.evolutiongenerator.utils.StringOptionParser;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class SimulationStage extends Application {

    private AbstractWorldMap map;
    private Vector2d[] mapElementPositions;
    private SimulationEngine simulationEngine;
    private Integer cellWidth = 50;
    private Integer cellHeight = 50;
    private Double borderWidth = 1.2;
    private GridPane grid = new GridPane();
    private Vector2d[] mapBounds;

    public SimulationStage(Map<ConfigurationConstant, ISimulationConfigurationValue> args, Stage primaryStage) {
        args.keySet().forEach(key -> System.out.println(key + " " + args.get(key)));
        init(new String[]{});
        start(primaryStage);
    }

    public void init(String[] args) throws IllegalArgumentException {
        try {
            MoveDirection[] directions = new StringOptionParser().parse(args);
            this.mapElementPositions = new Vector2d[]{new Vector2d(2, 2), new Vector2d(3, 4)};
            this.map = new GrassField(10);
            this.simulationEngine = new SimulationEngine(map, mapElementPositions, directions, this, 1000);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }
    }

    private void startAnimation(TextField textField) {
        String[] args = textField.getText().split(" ");
        simulationEngine.setDirectionArray(new StringOptionParser().parse(args));
        Thread engineThread = new Thread(simulationEngine);
        engineThread.start();
    }

    private void setIcon(Stage stage) throws FileNotFoundException {
        try {
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/src/main/resources/");
            Image image = new Image(new FileInputStream(filePath.concat("head_south.png")));
            stage.getIcons().add(image);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public void start(Stage primaryStage) {
        mapBounds = map.getMapBounds();
        grid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        grid.getRowConstraints().add(new RowConstraints(cellHeight));
        for (int x = 1; x <= mapBounds[1].x - mapBounds[0].x + 1; x++) {
            grid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int y = 1; y <= mapBounds[1].y - mapBounds[0].y + 1; y++) {
            grid.getRowConstraints().add(new RowConstraints(cellHeight));
        }

        renderGrid();

        HBox guiInterface = new HBox(5);
        guiInterface.setAlignment(Pos.CENTER);
        guiInterface.setPadding(new Insets(10, 0, 0, 0));
        Button animationStart = new Button("Start");
        TextField animationMoves = new TextField();
        animationStart.setOnAction((action) -> {
            startAnimation(animationMoves);
        });

        guiInterface.getChildren().addAll(animationMoves, animationStart);

        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().addAll(grid, guiInterface);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(20));

        // Detect width of map and create scene with this value
//        Scene scene = new Scene(grid, Math.min(1300, (mapBounds[1].x - mapBounds[0].x + 2) * (cellWidth + 1)),
//                Math.min(1300, (mapBounds[1].y - mapBounds[0].y + 2) * (cellHeight + 1)));
        Scene scene = new Scene(scrollPane, 800, 800);
        primaryStage.setTitle("WorldMap");
        try {
            setIcon(primaryStage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void renderGrid() {
        String inlineStyle = "-fx-border-color: darkgray; " +
                "-fx-min-width: " + cellWidth + ";" +
                "-fx-min-height:" + cellHeight + ";";

        grid.getChildren().clear();
//        grid.setGridLinesVisible(true); // Replaced with custom inline-css because why not :)
        grid.setAlignment(Pos.CENTER);

        mapBounds = map.getMapBounds();

        // For debug purposes
//        System.out.println(map);

        // Iinitialize coordinates
        Label label = new Label("y/x");
        label.setAlignment(Pos.CENTER);
        label.setStyle(inlineStyle + "-fx-border-width:" + borderWidth + ";");
        grid.add(label, 0, 0);

        Integer value = mapBounds[0].x;
        for (int x = 1; x <= mapBounds[1].x - mapBounds[0].x + 1; x++) {
            label = new Label(value.toString());
            label.setStyle(inlineStyle + "-fx-border-width:" + borderWidth + " " + borderWidth + " " + borderWidth + " 0;");
            label.setAlignment(Pos.CENTER);
            grid.add(label, x, 0);
            value += 1;
        }

        value = mapBounds[1].y;
        for (int y = 1; y <= mapBounds[1].y - mapBounds[0].y + 1; y++) {
            label = new Label(value.toString());
            label.setStyle(inlineStyle + "-fx-border-width: 0 " + borderWidth + " " + borderWidth + " " + borderWidth + ";");
            label.setAlignment(Pos.CENTER);
            grid.add(label, 0, y);
            value -= 1;
        }

        inlineStyle += "-fx-border-width: 0 " + borderWidth + " " + borderWidth + " 0;";

        int yCoord = mapBounds[1].y;
        for (int y = 1; y <= mapBounds[1].y - mapBounds[0].y + 1; y++) {
            int xCoord = mapBounds[0].x;
            for (int x = 1; x <= mapBounds[1].x - mapBounds[0].x + 1; x++) {
                // Translate y coord, because (0,0) is in the upper left corner
                Object object = map.objectAt(new Vector2d(xCoord, yCoord));
                if (object == null) {
                    label = new Label(" ");
                    label.setStyle(inlineStyle);
                    grid.add(label, x, y);
                } else {
                    try {
                        IMapElement element = (IMapElement) object;
                        GuiElementBox mapElement = new GuiElementBox(element);
                        mapElement.getNode().setStyle(inlineStyle);
                        mapElement.getNode().setAlignment(Pos.CENTER);
                        grid.add(mapElement.getNode(), x, y);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                xCoord += 1;
            }
            yCoord -= 1;
        }
    }
}

