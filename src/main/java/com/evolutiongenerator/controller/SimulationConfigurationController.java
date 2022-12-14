package com.evolutiongenerator.controller;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.handler.ChoiceBoxHandler;
import com.evolutiongenerator.handler.IConfigurationField;
import com.evolutiongenerator.handler.TextFieldHandler;
import com.evolutiongenerator.stage.SimulationStage;
import com.evolutiongenerator.utils.FileChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Controller for simulation configuration window.
 *
 * @author Patryk Klatka
 */
public class SimulationConfigurationController implements Initializable {

    // ********** Path configuration **********
    private final String configurationsFolderPath = "/src/main/resources/simulation/configurations/";
    private final ExtensionFilter txtExtensionFilter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
    private final ExtensionFilter csvExtensionFilter = new ExtensionFilter("CSV files (*.csv)", "*.csv");


    // ********** Configuration for ChoiceBox objects **********
    List<String> exampleConfigurations;

    // ************* Properties *************
    private String statisticsFileLocationURL = "";
    private boolean stopListeningExampleConfiguration = false;
    private final HashMap<ConfigurationConstant, IConfigurationField> simulationProperties = new HashMap<>();
    private FileChooser fileChooser;

    // ********** Configuration fields **********
    @FXML
    private Button runSimulation;
    @FXML
    private Button saveConfiguration;
    @FXML
    private Button loadConfiguration;
    @FXML
    private ChoiceBox<String> exampleConfiguration;

    // ********** Map fields **********
    @FXML
    private TextField mapWidth;
    @FXML
    private TextField mapHeight;
    @FXML
    private ChoiceBox<String> mapVariant;

    // ********** Animal fields **********
    @FXML
    private TextField animalStartNumber;
    @FXML
    private TextField genomLength;
    @FXML
    private TextField animalStartEnergy;
    @FXML
    private TextField animalCreationEnergy;
    @FXML
    private TextField animalCreationEnergyConsumption;
    @FXML
    private ChoiceBox<String> animalBehaviourVariant;

    // ********** Plants fields **********
    @FXML
    private TextField plantStartNumber;
    @FXML
    private TextField plantEnergy;
    @FXML
    private TextField plantSpawnNumber;
    @FXML
    private ChoiceBox<String> plantGrowVariant;

    // ********** Mutations fields **********
    @FXML
    private TextField minimumMutationNumber;
    @FXML
    private TextField maximumMutationNumber;
    @FXML
    private ChoiceBox<String> mutationVariant;

    // ********** Options fields **********
    @FXML
    private CheckBox saveStatistics;
    @FXML
    private Button statisticsFileLocation;
    @FXML
    private Label statisticsFileLocationStatus;


    // ************* Setters *************

    /**
     * Sets the fileChooser utility from simulationConfiguration stage.
     *
     * @author Patryk Klatka
     * @param fileChooser reference to fileChooser utility
     */
    public void setFileChooserUtil(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }


    // ************* Getters *************

    /**
     * Get path to the example configuration file.
     *
     * @author Patryk Klatka
     * @param filename name of the example configuration file
     * @return path to file
     */
    private String getExampleConfigurationPath(String filename) {
        if (filename == null || filename.equals("")) {
            return "";
        }

        return new File("").getAbsolutePath().concat(configurationsFolderPath + filename + ".txt");
    }

    /**
     * Gets saved options as HashMap.
     *
     * @author Patryk Klatka
     * @return HashMap with saved options
     */
    private HashMap<ConfigurationConstant, Object> getSimulationOptions() {
        HashMap<ConfigurationConstant, Object> args = new HashMap<>();
        // ******* Additional arguments *******
        args.put(ConfigurationConstant.STATISTICS_FILE_PATH, statisticsFileLocationURL);

        for (ConfigurationConstant key : simulationProperties.keySet()) {
            Object property = simulationProperties.get(key).readProperty();
            if (property.equals("")) {
                alert(Alert.AlertType.ERROR, "Błąd parametru", "Error", "Parametr " + key + " ma błędną wartość");
                return null;
            }
            args.put(key, property);
        }
        return args;
    }

    // ************* File utilities *************

    /**
     * Loads configuration from filePath.
     *
     * @author Patryk Klatka
     * @param filePath path to file
     */
    private void loadConfiguration(String filePath) throws IllegalArgumentException, IOException {
        if (filePath.equals("")) {
            return;
        }

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach((line) -> {
                // Parse line and update values in GUI
                if (line.contains("=")) {
                    String[] propPair = line.split("=");
                    if (propPair.length < 2) {
                        throw new IllegalArgumentException("Zły argument " + propPair[0] + " w pliku " + filePath);
                    }
                    IConfigurationField field = simulationProperties.get(ConfigurationConstant.valueOf(propPair[0].trim()));
                    if (field != null) {
                        if (propPair[1].equals("")) {
                            throw new IllegalArgumentException("Parametr " + propPair[0] + " ma błędną wartość w pliku " + filePath);
                        }
                        field.writeProperty(propPair[1].trim());
                    } else {
                        throw new IllegalArgumentException("Zły argument " + propPair[0] + " w pliku " + filePath);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Odczyt pliku", "Error", "Błąd odczytu pliku: " + e.getLocalizedMessage());
            throw new IOException(e);
        }
    }

    /**
     * Saves current configuration to file.
     *
     * @author Patryk Klatka
     * @param filePath path to file
     */
    private void saveConfiguration(String filePath) throws IOException {
        try {
            ArrayList<String> lines = new ArrayList<>();

            // Read data
            for (ConfigurationConstant key : simulationProperties.keySet()) {
                Object property = simulationProperties.get(key).readProperty();
                if (property.equals("")) {
                    alert(Alert.AlertType.ERROR, "Błąd parametru", "Error", "Parametr " + key + " ma błędną wartość");
                    return;
                }
                lines.add(key + "=" + property);
            }

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath);
            Files.write(path, lines, utf8, StandardOpenOption.CREATE);

            alert(Alert.AlertType.INFORMATION, "Zapis pliku", "Informacja", "Plik został zapisany");
        } catch (IOException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Zapis pliku", "Error", "Błąd zapisu pliku: " + e.getLocalizedMessage());
            throw new IOException(e);
        }
    }

    // ************* Handlers *************

    /**
     * Handles exampleConfiguration button.
     *
     * @author Patryk Klatka
     */
    private void exampleConfigurationHandler(ActionEvent event){
        try {
            if (stopListeningExampleConfiguration) {
                stopListeningExampleConfiguration = false;
                return;
            }

            if (exampleConfiguration.getValue().equals("")) {
                return;
            }

            String filename = exampleConfiguration.getValue();
            loadConfiguration(getExampleConfigurationPath(filename));

            stopListeningExampleConfiguration = true;
            exampleConfiguration.setValue(filename);
        } catch (IOException | IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }

    /**
     * Handles loadConfiguration button.
     *
     * @author Patryk Klatka
     */
    private void loadConfigurationHandler(ActionEvent event){
        try {
            String path = fileChooser.getFilePath(txtExtensionFilter);

            // User has clicked cancel button
            if (path == null || path.equals("")) {
                return;
            }
            loadConfiguration(path);
            exampleConfiguration.setValue("");
        } catch (IOException | IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }

    /**
     * Handles saveConfiguration button.
     *
     * @author Patryk Klatka
     */
    private void saveConfigurationHandler(ActionEvent event){
        try {
            String path = fileChooser.saveFilePath(txtExtensionFilter);

            // User has clicked cancel button
            if (path.equals("")) {
                return;
            }

            saveConfiguration(path);
        } catch (IOException e) {
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }

    /**
     * Handles statisticsFileLocation button.
     *
     * @author Patryk Klatka
     */
    private void statisticsFileLocationHandler(ActionEvent event){
        if (saveStatistics.isSelected()) {
            statisticsFileLocationURL = fileChooser.saveFilePath(csvExtensionFilter);
            if (statisticsFileLocationURL.equals("")) {
                statisticsFileLocationStatus.setText("Nie podano lokalizacji!");
            } else {
                statisticsFileLocationStatus.setText("Zapisano lokalizację");
            }
        } else {
            statisticsFileLocation.setDisable(true);
            statisticsFileLocationStatus.setText("");
        }
    }

    /**
     * Handles saveStatistics checkbox.
     *
     * @author Patryk Klatka
     */
    private void saveStatisticsHandler(ActionEvent event){
        if (saveStatistics.isSelected()) {
            statisticsFileLocation.setDisable(false);
        } else {
            statisticsFileLocation.setDisable(true);
            statisticsFileLocationStatus.setText("");
            statisticsFileLocationURL = "";
        }
    }

    /**
     * Handles runSimulation button.
     *
     * @author Patryk Klatka
     */
    private void runSimulationHandler(ActionEvent event){
        // Get all arguments
        HashMap<ConfigurationConstant, Object> args = getSimulationOptions();

        if (args == null) {
            return;
        }

        // Send arguments to simulation stage
        new SimulationStage(args, new Stage());
    }

    // ************* Initialization *************

    /**
     * Loads example configurations from resources folder and initializes variables which handle form.
     *
     * @author Patryk Klatka
     */
    private void loadProperties() throws IOException {
        // Load files from configurationsFolderPath directory
        String filePath = new File("").getAbsolutePath().concat(configurationsFolderPath);

        try (Stream<Path> stream = Files.list(Paths.get(filePath))) {
            exampleConfigurations = stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).map((fileName) -> {
                // Remove extension from file name
                int pos = fileName.lastIndexOf(".");
                if (pos > 0 && pos < (fileName.length() - 1)) {
                    fileName = fileName.substring(0, pos);
                }
                return fileName;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        // ********* TextField
        simulationProperties.put(ConfigurationConstant.MAP_WIDTH, new TextFieldHandler(mapWidth, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.MAP_HEIGHT, new TextFieldHandler(mapHeight, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.ANIMAL_START_NUMBER, new TextFieldHandler(animalStartNumber, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.GENOTYPE_LENGTH, new TextFieldHandler(genomLength, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.ANIMAL_START_ENERGY, new TextFieldHandler(animalStartEnergy, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY, new TextFieldHandler(animalCreationEnergy, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST, new TextFieldHandler(animalCreationEnergyConsumption, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.PLANT_START_NUMBER, new TextFieldHandler(plantStartNumber, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.PLANT_ENERGY, new TextFieldHandler(plantEnergy, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.PLANT_SPAWN_NUMBER, new TextFieldHandler(plantSpawnNumber, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.MINIMUM_MUTATION_NUMBER, new TextFieldHandler(minimumMutationNumber, exampleConfiguration, Integer::valueOf));
        simulationProperties.put(ConfigurationConstant.MAXIMUM_MUTATION_NUMBER, new TextFieldHandler(maximumMutationNumber, exampleConfiguration, Integer::valueOf));

        // ********* ChoiceBox<String>
        simulationProperties.put(ConfigurationConstant.MAP_VARIANT, new ChoiceBoxHandler(mapVariant, exampleConfiguration, MapVariant::fromString));
        simulationProperties.put(ConfigurationConstant.ANIMAL_BEHAVIOUR_VARIANT, new ChoiceBoxHandler(animalBehaviourVariant, exampleConfiguration, AnimalBehaviourVariant::fromString));
        simulationProperties.put(ConfigurationConstant.PLANT_GROWTH_VARIANT, new ChoiceBoxHandler(plantGrowVariant, exampleConfiguration, PlantGrowthVariant::fromString));
        simulationProperties.put(ConfigurationConstant.MUTATION_VARIANT, new ChoiceBoxHandler(mutationVariant, exampleConfiguration, MutationVariant::fromString));
    }


    /**
     * Initializes ChoiceBox from List of strings.
     *
     * @author Patryk Klatka
     * @param choiceBox ChoiceBox to initialize
     * @param values List of strings to initialize ChoiceBox
     */
    private void initializeChoiceBox(ChoiceBox<String> choiceBox, List<String> values) {
        choiceBox.getItems().addAll(values);
        choiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Initializes SimulationConfiguration handlers, listeners, variables.
     *
     * @author Patryk Klatka
     * @param location URL
     * @param resources ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadProperties();

            // Initialize ChoiceBox objects
            initializeChoiceBox(animalBehaviourVariant, AnimalBehaviourVariant.getValuesAsStringList());
            initializeChoiceBox(mapVariant, MapVariant.getValuesAsStringList());
            initializeChoiceBox(plantGrowVariant, PlantGrowthVariant.getValuesAsStringList());
            initializeChoiceBox(mutationVariant, MutationVariant.getValuesAsStringList());
            initializeChoiceBox(exampleConfiguration, exampleConfigurations);

            // Load default configuration
            String defaultConfigurationFilename = exampleConfiguration.getValue();
            loadConfiguration(getExampleConfigurationPath(defaultConfigurationFilename));
            exampleConfiguration.setValue(defaultConfigurationFilename);

            // Initialize button handlers
            exampleConfiguration.setOnAction(this::exampleConfigurationHandler);
            loadConfiguration.setOnAction(this::loadConfigurationHandler);
            saveConfiguration.setOnAction(this::saveConfigurationHandler);
            statisticsFileLocation.setOnAction(this::statisticsFileLocationHandler);
            saveStatistics.setOnAction(this::saveStatisticsHandler);
            runSimulation.setOnAction(this::runSimulationHandler);

        } catch (IOException | IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }


    // ************* Utils *************

    /**
     * Shows alert with given parameters.
     *
     * @author Patryk Klatka
     * @param alertType Alert type
     * @param title Alert title
     * @param header Alert header
     * @param content Alert content
     */
    private void alert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
