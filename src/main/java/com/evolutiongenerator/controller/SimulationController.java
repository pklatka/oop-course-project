package com.evolutiongenerator.controller;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.model.engine.IEngine;
import com.evolutiongenerator.model.engine.SimulationEngine;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.model.ui.GuiMapElement;
import com.evolutiongenerator.model.ui.SortedListViewRecord;
import com.evolutiongenerator.stage.ISimulationObserver;
import com.evolutiongenerator.utils.DoubleValue;
import com.evolutiongenerator.utils.IntegerValue;
import com.evolutiongenerator.utils.StringValue;
import com.evolutiongenerator.utils.Vector2d;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;

/**
 * Controller for simulation stage
 *
 * @author Patryk Klatka
 */
public class SimulationController implements Initializable, ISimulationObserver {

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

    private int mapWidth = 10;
    private int mapHeight = 10;
    private double cellWidth; // = gridWidth / mapWidth;
    private double cellHeight; // = gridHeight / mapHeight;
    private Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions;
    private ObservableList<SortedListViewRecord> mostPopularGenomes = FXCollections.observableArrayList(rec -> new Observable[]{rec.priority});
    private final Map<String, SortedListViewRecord> genomeRecord = new HashMap<>();
    private final Map<String, List<GuiMapElement>> mapElementsWithSameGenome = new HashMap<>();
    private final Map<Vector2d, StackPane> mapFields = new HashMap<>();
    private final Map<IMapElement, Pair<Vector2d, GuiMapElement>> elementProperties = new HashMap<>();
    boolean isSimulationRunning = false;
    GuiMapElement selectedAnimal = null;
    IEngine engine;
    Thread simulationThread;

    // ****** Setters ******

    /**
     * Sets simulation arguments
     *
     * @param simulationOptions Simulation arguments
     */
    public void setSimulationOptions(Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions) {
        this.simulationOptions = simulationOptions;
    }

    /**
     * Sets simulation options
     */
    private void setSimulationOptions() {
        for (ConfigurationConstant configurationConstant : simulationOptions.keySet()) {
            if (Objects.requireNonNull(configurationConstant.getType()) == ConfigurationConstant.ConfigurationConstantType.INTEGER) {
                IntegerValue integerValue = (IntegerValue) simulationOptions.get(configurationConstant);
                switch (configurationConstant) {
                    case MAP_WIDTH -> mapWidth = integerValue.getValue();
                    case MAP_HEIGHT -> mapHeight = integerValue.getValue();
                    case SIMULATION_COUNTER -> simulationTitle.setText("Symulacja nr " + integerValue.getValue());
                }
            }
        }
    }

    // ****** Handlers ******

    /**
     * Handles simulation control button
     *
     * @param actionEvent Action event
     */
    private void simulationControlButtonHandler(ActionEvent actionEvent) {
        if (simulationControlButton.getText().equals("Start symulacji")) {
            simulationControlButton.setText("Stop symulacji");
            isSimulationRunning = true;
            resetPopularGenomes();

            // Get selected animal and send it to simulation
            if(selectedAnimal != null && selectedAnimal.getMapElement() instanceof Animal animal){
                engine.selectAnimalToObserve(animal);
            }else{
                resetSelectedAnimal();
                resetAnimalStatistics();
            }

            engine.resume();
        } else {
            simulationControlButton.setText("Start symulacji");
            isSimulationRunning = false;
            engine.pause();
        }
    }

    /**
     * Handles popular genomes list view
     *
     * @param observable Observable
     */
    private void popularGenomesHandler(Observable observable, SortedListViewRecord oldValue, SortedListViewRecord newValue) {
        if (isSimulationRunning) {
            return;
        }

        if (oldValue != null) {
            mapElementsWithSameGenome.get(oldValue.value.get()).forEach(GuiMapElement::unselectMapElement);
        }

        resetSelectedAnimal();

        if (newValue != null) {
            mapElementsWithSameGenome.get(newValue.value.get()).forEach(GuiMapElement::selectMapElement);
        }
    }

    private void mapElementMouseClickHandler(MouseEvent event) {
        if (isSimulationRunning) {
            return;
        }

        // Reset ListView
        resetPopularGenomes();

        GuiMapElement clickedElement = (GuiMapElement) event.getSource();
        if (clickedElement.equals(selectedAnimal)) {
            resetSelectedAnimal();
        } else {
            if (selectedAnimal != null) {
                selectedAnimal.unselectMapElement();
            }
            selectedAnimal = (GuiMapElement) event.getSource();
            selectedAnimal.selectMapElement();
            animalStatisticsStatus.setText("Wybrano zwierzę do śledzenia");
        }
    }

    // ****** Utils ******

    /**
     * Resets selected animals on map
     */
    private void resetSelectedAnimal() {
        if (selectedAnimal != null) {
            selectedAnimal.unselectMapElement();
            engine.selectAnimalToObserve(null);
            selectedAnimal = null;
        }

        animalStatisticsStatus.setText("Wybierz zwierzę, aby śledzić jego statystyki");
    }

    /**
     * Resets popular genomes list view
     */
    private void resetPopularGenomes() {
        if (popularGenomes.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        mapElementsWithSameGenome.get(popularGenomes.getSelectionModel().getSelectedItem().value.get()).forEach(GuiMapElement::unselectMapElement);
        popularGenomes.getSelectionModel().clearSelection();
    }

    // ****** Initializers ******

    /**
     * Initializes simulation map
     */
    private void initializeMap() {
        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);
        grid.setStyle("-fx-border-color: #003d00; -fx-border-width: 1px;");

        for (int i = 0; i < mapWidth; i++) {
            ColumnConstraints column = new ColumnConstraints(cellWidth);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < mapHeight; i++) {
            RowConstraints row = new RowConstraints(cellHeight);
            grid.getRowConstraints().add(row);
        }

        // Initialize mapFields (hash)map
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.setStyle("-fx-background-color: #7fef7f");
                grid.add(stackPane, i, j);
                mapFields.put(new Vector2d(i, j), stackPane);
            }
        }

        grid.setAlignment(Pos.CENTER);
        mapContainer.getChildren().add(grid);
        map = grid;
    }

    /**
     * Initializes simulation stage
     */
    private void initializeStage() {
        // Read arguments
        setSimulationOptions();

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

        // Initialize engine
        SimulationEngine engineToRun = new SimulationEngine(simulationOptions, this);
        engine = engineToRun;

        // Start engine thread
        simulationThread = new Thread(engineToRun);
        simulationThread.start();
    }


    /**
     * Main method for initializing simulation stage
     *
     * @param location  URL
     * @param resources Resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initializeStage);
    }

    /**
     * Closes simulation stage
     */
    public void exit(WindowEvent event){
        engine.kill();
//        simulationThread.interrupt();
    }

    // ****** Render utils ******

    /**
     * Render simulation statistics.
     *
     * @param essentialStatistics Statistics to show in GUI.
     */
    @Override
    public void renderMainStatistics(Map<SimulationStatistics, ISimulationConfigurationValue> essentialStatistics) {
        for (SimulationStatistics configurationConstant : essentialStatistics.keySet()) {
            ISimulationConfigurationValue value = essentialStatistics.get(configurationConstant);
            if (value instanceof IntegerValue integerValue) {
                switch (configurationConstant) {
                    case DAY -> day.setText(integerValue.toString());
                    case NUMBER_OF_ANIMALS -> numberOfAnimals.setText(integerValue.toString());
                    case NUMBER_OF_PLANTS -> numberOfPlants.setText(integerValue.toString());
                    case NUMBER_OF_EMPTY_FIELDS -> numberOfEmptyFields.setText(integerValue.toString());
                }
            }else if (value instanceof DoubleValue doubleValue) {
                switch (configurationConstant) {
                    case AVERAGE_ANIMAL_LIFESPAN -> averageAnimalLifespan.setText(doubleValue.toString());
                    case AVERAGE_ANIMAL_ENERGY -> averageAnimalEnergy.setText(doubleValue.toString());
                }
            }
        }
    }

    /**
     * Add element to map (GridPane).
     *
     * @param mapElement Element to add.
     */
    @Override
    public boolean addElementToMap(IMapElement mapElement, Vector2d position, boolean selectMapElement) {
        try {
            Vector2d mapPosition = new Vector2d(position.x, mapHeight - 1 - position.y);
            StackPane stackPane = mapFields.get(mapPosition);
            if(stackPane == null){
                return false;
            }

            GuiMapElement guiMapElement = new GuiMapElement(cellWidth, cellHeight, mapElement, simulationOptions);
            guiMapElement.setOnMouseClicked(this::mapElementMouseClickHandler);

            if(selectMapElement){
                guiMapElement.selectMapElement();
                selectedAnimal = guiMapElement;
            }

            if(mapElement instanceof Plant){
                stackPane.getChildren().add(0, guiMapElement);
            }else{
                stackPane.getChildren().add(guiMapElement);
            }
            elementProperties.put(mapElement, new Pair<>(mapPosition, guiMapElement));

            if (mapElement instanceof Animal) {
                addGenomeToPopularGenomes((Animal) mapElement);
            }

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Remove element from map (GridPane).
     *
     * @param mapElement Element to remove.
     */
    @Override
    public boolean removeElementFromMap(IMapElement mapElement) {
        Pair<Vector2d, GuiMapElement> properties = elementProperties.get(mapElement);
        if (properties == null) {
            return false;
        }

        if (mapElement instanceof Animal) {
            removeGenomeFromPopularGenomes((Animal) mapElement);
        }

        Vector2d mapPosition = properties.getKey();
        GuiMapElement guiMapElement = properties.getValue();
        mapFields.get(mapPosition).getChildren().remove(guiMapElement);
        elementProperties.remove(mapElement);

        return true;
    }

    /**
     * Change element position on map (GridPane).
     * Note: you can only change position of the animal.
     *
     * @param mapElement  Element to change position.
     * @param newPosition New position of element.
     */
    @Override
    public void changeElementPositionOnMap(IMapElement mapElement, Vector2d newPosition) throws IllegalArgumentException {
        if (!(mapElement instanceof Animal)) {
            throw new IllegalArgumentException("Można zmieniać tylko pozycję zwierzęcia.");
        }

        removeElementFromMap(mapElement);
        addElementToMap(mapElement, newPosition, false);
    }

    /**
     * Adds genome to ListView with popular genomes.
     *
     * @param animal Animal which has genome with toString method.
     */
    @Override
    public void addGenomeToPopularGenomes(Animal animal) {
        if (!genomeRecord.containsKey(animal.getGenome().toString())) {
            genomeRecord.put(animal.getGenome().toString(), new SortedListViewRecord(1, animal.getGenome().toString()));
            List<GuiMapElement> list = new ArrayList<>();
            list.add(elementProperties.get(animal).getValue());
            mapElementsWithSameGenome.put(animal.getGenome().toString(), list);
            mostPopularGenomes.add(genomeRecord.get(animal.getGenome().toString()));
        } else {
            genomeRecord.get(animal.getGenome().toString()).priority.set(genomeRecord.get(animal.getGenome().toString()).priority.get() + 1);
            mapElementsWithSameGenome.get(animal.getGenome().toString()).add(elementProperties.get(animal).getValue());
        }
    }

    /**
     * Removes genome from ListView with popular genomes.
     *
     * @param animal Animal which has genome with toString method.
     */
    @Override
    public void removeGenomeFromPopularGenomes(Animal animal) {
        if (!genomeRecord.containsKey(animal.getGenome().toString())) {
            return;
        }

        if (genomeRecord.get(animal.getGenome().toString()).priority.get() == 1) {
            mostPopularGenomes.remove(genomeRecord.get(animal.getGenome().toString()));
            genomeRecord.remove(animal.getGenome().toString());
            mapElementsWithSameGenome.remove(animal.getGenome().toString());
            return;
        }

        if (genomeRecord.get(animal.getGenome().toString()).priority.get() > 1) {
            genomeRecord.get(animal.getGenome().toString()).priority.set(genomeRecord.get(animal.getGenome().toString()).priority.get() - 1);
            mapElementsWithSameGenome.get(animal.getGenome().toString()).remove(elementProperties.get(animal).getValue());
            return;
        }
    }

    /**
     * Updates animal statistics if animal was selected when simulation was paused.
     *
     * @param animalStatistics Animal statistics.
     */
    @Override
    public void updateAnimalStatistics(Map<AnimalStatistics, ISimulationConfigurationValue> animalStatistics) {
        for (AnimalStatistics configurationConstant : animalStatistics.keySet()) {
            ISimulationConfigurationValue value = animalStatistics.get(configurationConstant);
            if (value instanceof IntegerValue integerValue) {
                switch (configurationConstant) {
                    case ANIMAL_ACTIVE_GENOME -> selectedAnimalActiveGenome.setText(integerValue.toString());
                    case ANIMAL_ENERGY -> selectedAnimalEnergy.setText(integerValue.toString());
                    case ANIMAL_LIFESPAN -> selectedAnimalLifespan.setText(integerValue.toString());
                    case ANIMAL_NUMBER_OF_CHILDREN -> selectedAnimalNumberOfChildren.setText(integerValue.toString());
                    case ANIMAL_EATEN_PLANTS -> selectedAnimalEatenPlants.setText(integerValue.toString());
                }
            } else if (value instanceof StringValue stringValue) {
                switch (configurationConstant) {
                    case ANIMAL_GENOME -> selectedAnimalGenome.setText(stringValue.toString());
                    case ANIMAL_DEATH_DAY -> selectedAnimalDeathDay.setText(stringValue.toString());
                }
            }
        }
    }

    public void resetAnimalStatistics(){
        Map<AnimalStatistics, ISimulationConfigurationValue> animalStatistics = new HashMap<>();
        animalStatistics.put(AnimalStatistics.ANIMAL_ACTIVE_GENOME, new IntegerValue(0));
        animalStatistics.put(AnimalStatistics.ANIMAL_ENERGY, new IntegerValue(0));
        animalStatistics.put(AnimalStatistics.ANIMAL_LIFESPAN, new IntegerValue(0));
        animalStatistics.put(AnimalStatistics.ANIMAL_NUMBER_OF_CHILDREN, new IntegerValue(0));
        animalStatistics.put(AnimalStatistics.ANIMAL_EATEN_PLANTS, new IntegerValue(0));
        animalStatistics.put(AnimalStatistics.ANIMAL_GENOME, new StringValue("-"));
        animalStatistics.put(AnimalStatistics.ANIMAL_DEATH_DAY, new StringValue("b. d."));
        updateAnimalStatistics(animalStatistics);
    }
}
