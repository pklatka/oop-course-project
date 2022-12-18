package com.evolutiongenerator.controller;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.constant.IntegerValue;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;


public class SimulationController implements Initializable {

    // ***** Simulation statistics fields *****
    @FXML
    Label simulationTitle;
    @FXML
    Label day;
    @FXML
    Label numberOfAnimals;
    @FXML
    Label numberOfPlants;
    @FXML
    Label numberOfEmptyFields;
    @FXML
    Label averageAnimalEnergy;
    @FXML
    Label averageAnimalLifespan;
    @FXML
    ListView<SortedListViewRecord> popularGenomes;

    // ***** Animal statistics fields *****
    @FXML
    Label animalStatisticsStatus;
    @FXML
    Label selectedAnimalGenome;
    @FXML
    Label selectedAnimalActiveGenome;
    @FXML
    Label selectedAnimalEnergy;
    @FXML
    Label selectedAnimalEatenPlants;
    @FXML
    Label selectedAnimalNumberOfChildren;
    @FXML
    Label selectedAnimalLifespan;
    @FXML
    Label selectedAnimalDeathDay;

    // ***** Simulation map fields *****
    @FXML
    Button simulationControlButton;
    @FXML
    VBox mapContainer;
    GridPane map;

    // ***** Simulation map configuration *****
    private double gridWidth; // = mapContainer.getMinWidth();
    private double gridHeight; // = mapContainer.getMinHeight()

    private double mapWidth = 10;
    private double mapHeight = 10;

    private double cellWidth; // = gridWidth / mapWidth;
    private double cellHeight; // = gridHeight / mapHeight;
    private Map<ConfigurationConstant, ISimulationConfigurationValue> args;
    private ObservableList<SortedListViewRecord> mostPopularGenomes = FXCollections.observableArrayList(rec -> new Observable[]{rec.priority});
    public void setArgs(Map<ConfigurationConstant, ISimulationConfigurationValue> args) {
        this.args = args;
    }

    private void readArguments() {
        for(ConfigurationConstant configurationConstant: args.keySet()){
            switch (configurationConstant.getType()){
                case INTEGER -> {
                    IntegerValue integerValue = (IntegerValue) args.get(configurationConstant);
                    switch (configurationConstant) {
                        case MAP_WIDTH -> mapWidth = integerValue.getValue();
                        case MAP_HEIGHT -> mapHeight = integerValue.getValue();
                    }
                }
                case PATH -> {}
                case MAP_VARIANT -> {}
                case MUTATION_VARIANT -> {}
                case PLANT_GROWTH_VARIANT -> {}
                case ANIMAL_BEHAVIOUR_VARIANT -> {}
            }
        }}

    private void initializeMap(){
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        for (int i = 0; i < mapWidth; i++) {
            ColumnConstraints column = new ColumnConstraints(cellWidth);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < mapHeight; i++) {
            RowConstraints row = new RowConstraints(cellHeight);
            grid.getRowConstraints().add(row);
        }

        grid.setAlignment(Pos.CENTER);
        mapContainer.getChildren().add(grid);
        map = grid;
    }


    private void initializeStage(){
        // Read arguments
        readArguments();

        // Initialize properties
        gridWidth = mapContainer.getMinWidth();
        gridHeight = mapContainer.getMinHeight();

        cellWidth = gridWidth / mapWidth;
        cellHeight = gridHeight / mapHeight;

        // Initialize popularGenomes ListView
        popularGenomes.setItems(mostPopularGenomes.sorted(Comparator.comparingInt(l -> -l.priority.get())));

        // Initialize listeners
        popularGenomes.getSelectionModel().selectedItemProperty().addListener(this::popularGenomesHandler);

        simulationControlButton.setOnAction(this::simulationControlButtonHandler);

        // Initialize map
        initializeMap();
    }

    private void simulationControlButtonHandler(ActionEvent actionEvent) {
        popularGenomes.getSelectionModel().clearSelection();
        mostPopularGenomes.add(new SortedListViewRecord(1, "Test"));
    }

    private void popularGenomesHandler(Observable observable) {
        SortedListViewRecord selectedRecord = popularGenomes.getSelectionModel().getSelectedItem();

        if (selectedRecord == null){
            return;
        }
        System.out.println(selectedRecord);

        // TODO: Select animals with selected genome
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initializeStage);
    }
}
