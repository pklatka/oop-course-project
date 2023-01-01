package com.evolutiongenerator.controller;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.handler.ChoiceBoxHandler;
import com.evolutiongenerator.handler.IConfigurationField;
import com.evolutiongenerator.handler.TextFieldHandler;
import com.evolutiongenerator.stage.SimulationStage;
import com.evolutiongenerator.utils.FileChooser;
import com.evolutiongenerator.utils.IntegerValue;
import com.evolutiongenerator.utils.PathValue;
import javafx.application.Platform;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Controller for simulation configuration stage.
 *
 * @author Patryk Klatka
 */
public class SimulationConfigurationController implements Initializable {

    // ********** Path configuration **********
    private final String configurationsFolderPath = "/src/main/resources/simulation/configurations/";
    private final ExtensionFilter txtExtensionFilter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
    private final ExtensionFilter csvExtensionFilter = new ExtensionFilter("CSV files (*.csv)", "*.csv");


    // ********** Configuration for ChoiceBox objects **********
    String[] exampleConfigurations;

    // ************* Properties *************
    private PathValue statisticsFileLocationURL = null;
    private boolean listenForIConfigurationFieldInput = false;
    private final Map<ConfigurationConstant, IConfigurationField> simulationProperties = new EnumMap<>(ConfigurationConstant.class);
    private FileChooser fileChooser;
    private final AtomicInteger simulationCounter = new AtomicInteger(0);

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
    private ChoiceBox<ISimulationConfigurationValue> mapVariant;

    // ********** Animal fields **********
    @FXML
    private TextField animalStartNumber;
    @FXML
    private TextField genomeLength;
    @FXML
    private TextField animalStartEnergy;
    @FXML
    private TextField animalCreationEnergy;
    @FXML
    private TextField animalCreationEnergyConsumption;
    @FXML
    private ChoiceBox<ISimulationConfigurationValue> animalBehaviourVariant;

    // ********** Plants fields **********
    @FXML
    private TextField plantStartNumber;
    @FXML
    private TextField plantEnergy;
    @FXML
    private TextField plantSpawnNumber;
    @FXML
    private ChoiceBox<ISimulationConfigurationValue> plantGrowVariant;

    // ********** Mutations fields **********
    @FXML
    private TextField minimumMutationNumber;
    @FXML
    private TextField maximumMutationNumber;
    @FXML
    private ChoiceBox<ISimulationConfigurationValue> mutationVariant;

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
     * @param fileChooser reference to fileChooser utility.
     */
    public void setFileChooserUtil(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }


    // ************* Getters *************

    /**
     * Get path to the example configuration file.
     *
     * @param filename name of the example configuration file.
     * @return path to file.
     */
    private String getExampleConfigurationPath(String filename) throws IllegalArgumentException {
        if (filename == null || filename.equals("")) {
            throw new IllegalArgumentException("Nazwa pliku przykładowej konfiguracji nie może być pusta.");
        }

        return new File("").getAbsolutePath().concat(configurationsFolderPath + filename + ".txt");
    }

    /**
     * Gets saved options as EnumMap.
     *
     * @return EnumMap with saved options.
     * @throws IllegalArgumentException if any of the options is not set or is invalid.
     */
    private Map<ConfigurationConstant, ISimulationConfigurationValue> getSimulationOptions() throws IllegalArgumentException {
        try {
            Map<ConfigurationConstant, ISimulationConfigurationValue> args = new EnumMap<>(ConfigurationConstant.class);
            // ******* Additional arguments *******
            args.put(ConfigurationConstant.STATISTICS_FILE_PATH, statisticsFileLocationURL);
            args.put(ConfigurationConstant.SIMULATION_COUNTER, new IntegerValue(simulationCounter.incrementAndGet()));

            for (ConfigurationConstant key : simulationProperties.keySet()) {
                args.put(key, simulationProperties.get(key).readProperty());
            }

            checkIfSimulationConfigurationIsValid();

            return args;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niepoprawna konfiguracja symulacji: " + e.getMessage());
        }
    }

    /**
     * Checks if simulation configuration is valid.
     *
     * @throws IllegalArgumentException if configuration field is invalid.
     */
    private void checkIfSimulationConfigurationIsValid() throws IllegalArgumentException {
        // Check integer values
        Integer mapWidth = ((IntegerValue) simulationProperties.get(ConfigurationConstant.MAP_WIDTH).readProperty()).getValue();
        Integer mapHeight = ((IntegerValue) simulationProperties.get(ConfigurationConstant.MAP_HEIGHT).readProperty()).getValue();

        if (!(0 < mapWidth && mapWidth <= 100)) {
            throw new IllegalArgumentException("Szerokość mapy musi być liczbą z przedziału (0, 100].");
        }

        if (!(0 < mapHeight && mapHeight <= 100)) {
            throw new IllegalArgumentException("Wysokość mapy musi być liczbą z przedziału (0, 100].");
        }

        Integer numberOfFields = mapWidth * mapHeight;

        Integer animalStartNumber = ((IntegerValue) simulationProperties.get(ConfigurationConstant.ANIMAL_START_NUMBER).readProperty()).getValue();

        if (animalStartNumber > numberOfFields) {
            throw new IllegalArgumentException("Liczba początkowych zwierząt nie może przekraczać liczby pól na mapie.");
        }

        if (animalStartNumber <= 0) {
            throw new IllegalArgumentException("Liczba początkowych zwierząt musi być większa od 0.");
        }

        Integer genotypeLength = ((IntegerValue) simulationProperties.get(ConfigurationConstant.GENOTYPE_LENGTH).readProperty()).getValue();
        if (genotypeLength <= 0) {
            throw new IllegalArgumentException("Długość genotypu musi być większa od 0.");
        }

        if (genotypeLength > 40) {
            throw new IllegalArgumentException("Długość genotypu nie może przekraczać 40.");
        }

        Integer animalStartEnergy = ((IntegerValue) simulationProperties.get(ConfigurationConstant.ANIMAL_START_ENERGY).readProperty()).getValue();

        if (animalStartEnergy <= 0) {
            throw new IllegalArgumentException("Początkowa energia zwierząt musi być większa od 0.");
        }

        Integer animalReproductionEnergy = ((IntegerValue) simulationProperties.get(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY).readProperty()).getValue();

        if (animalReproductionEnergy <= 0) {
            throw new IllegalArgumentException("Energia potrzebna do rozmnażania się zwierząt musi być większa od 0.");
        }

        Integer animalReproductionEnergyCost = ((IntegerValue) simulationProperties.get(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST).readProperty()).getValue();

        if (animalReproductionEnergyCost <= 0) {
            throw new IllegalArgumentException("Koszt rozmnażania się zwierząt musi być większy od 0.");
        }

        if (animalReproductionEnergy < animalReproductionEnergyCost) {
            throw new IllegalArgumentException("Energia potrzebna do rozmnażania się zwierząt musi być większa od kosztu rozmnażania się.");
        }

        Integer plantStartNumber = ((IntegerValue) simulationProperties.get(ConfigurationConstant.PLANT_START_NUMBER).readProperty()).getValue();

        if (plantStartNumber > numberOfFields) {
            throw new IllegalArgumentException("Liczba początkowych roślin nie może przekraczać liczby pól na mapie.");
        }

        if (plantStartNumber < 0) {
            throw new IllegalArgumentException("Liczba początkowych roślin musi być większa bądź równa 0.");
        }

        if (plantStartNumber > mapWidth * mapHeight) {
            throw new IllegalArgumentException("Liczba początkowych roślin nie może przekraczać liczby pól na mapie.");
        }

        Integer plantEnergy = ((IntegerValue) simulationProperties.get(ConfigurationConstant.PLANT_ENERGY).readProperty()).getValue();

        if (plantEnergy <= 0) {
            throw new IllegalArgumentException("Energia roślin musi być większa od 0.");
        }

        Integer plantSpawnNumber = ((IntegerValue) simulationProperties.get(ConfigurationConstant.PLANT_SPAWN_NUMBER).readProperty()).getValue();

        if (plantSpawnNumber < 0) {
            throw new IllegalArgumentException("Liczba roślin rozmnażających się w jednym dniu musi być większa bądź równa 0.");
        }

        Integer minimumMutationNumber = ((IntegerValue) simulationProperties.get(ConfigurationConstant.MINIMUM_MUTATION_NUMBER).readProperty()).getValue();

        if (minimumMutationNumber < 0) {
            throw new IllegalArgumentException("Minimalna liczba mutacji musi być większa bądź równa 0.");
        }

        Integer maximumMutationNumber = ((IntegerValue) simulationProperties.get(ConfigurationConstant.MAXIMUM_MUTATION_NUMBER).readProperty()).getValue();

        if (maximumMutationNumber < 0) {
            throw new IllegalArgumentException("Maksymalna liczba mutacji musi być większa bądź równa 0.");
        }

        if (maximumMutationNumber < minimumMutationNumber) {
            throw new IllegalArgumentException("Maksymalna liczba mutacji musi być większa bądź równa minimalnej liczbie mutacji.");
        }

    }

    // ************* File utilities *************

    /**
     * Loads configuration from filePath.
     *
     * @param filePath path to file.
     * @throws IOException              if file cannot be read.
     * @throws IllegalArgumentException if file is not valid.
     */
    private void loadConfiguration(String filePath) throws IOException, IllegalArgumentException {
        if (filePath == null || filePath.equals("")) {
            throw new IllegalArgumentException("Ścieżka ładowanego pliku nie może być pusta.");
        }

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach((line) -> {
                // Parse line and update values in GUI
                if (line.contains("=")) {
                    String[] propPair = line.split("=");
                    if (propPair.length < 2) {
                        throw new IllegalArgumentException("Zły format pliku " + filePath + ". Prawdopodobnie brakuje znaku '='.");
                    }

                    ConfigurationConstant configurationConstant = ConfigurationConstant.valueOf(propPair[0].trim());
                    IConfigurationField field = simulationProperties.get(configurationConstant);

                    if (field == null) {
                        throw new IllegalArgumentException("Zła wartość argumentu " + propPair[0] + " w pliku " + filePath + ".");
                    }

                    field.writeProperty(configurationConstant.getType().getValueFromString(propPair[1].trim()));
                }
            });
        } catch (IOException e) {
            throw new IOException("Nie udało się wczytać pliku " + filePath + ". " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Saves current configuration to file.
     *
     * @param filePath path to file.
     * @throws IOException if file cannot be written.
     */
    private void saveConfiguration(String filePath) throws IOException {
        try {
            ArrayList<String> lines = new ArrayList<>();

            // Save data as pairs of key and value separated by '='
            for (ConfigurationConstant key : simulationProperties.keySet()) {
                lines.add(key + "=" + simulationProperties.get(key).readProperty());
            }

            try {
                checkIfSimulationConfigurationIsValid();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Niepoprawna konfiguracja symulacji: " + e.getMessage());
            }

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath);
            Files.write(path, lines, utf8, StandardOpenOption.CREATE);

            alert(Alert.AlertType.INFORMATION, "Zapis pliku", "Informacja", "Plik został zapisany");
        } catch (InvalidPathException e) {
            throw new IOException("Błędna ścieżka do pliku z zapisem danych", e);
        } catch (IOException | IllegalArgumentException e) {
            throw new IOException(e);
        }
    }

    // ************* Handlers *************

    /**
     * Handles exampleConfiguration button.
     *
     * @param event ActionEvent.
     */
    private void exampleConfigurationHandler(ActionEvent event) {
        try {
            if (listenForIConfigurationFieldInput) {
                listenForIConfigurationFieldInput = false;
                return;
            }

            if (exampleConfiguration.getValue().equals("")) {
                return;
            }

            String filename = exampleConfiguration.getValue();
            loadConfiguration(getExampleConfigurationPath(filename));

            listenForIConfigurationFieldInput = true;
            exampleConfiguration.setValue(filename);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }

    /**
     * Handles loadConfiguration button.
     *
     * @param event ActionEvent.
     */
    private void loadConfigurationHandler(ActionEvent event) {
        try {
            String path = fileChooser.getFilePath(txtExtensionFilter);

            // User has clicked cancel button
            if (path == null) {
                return;
            }

            loadConfiguration(path);
            exampleConfiguration.setValue("");
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }

    /**
     * Handles saveConfiguration button.
     *
     * @param event ActionEvent.
     */
    private void saveConfigurationHandler(ActionEvent event) {
        try {
            String path = fileChooser.saveFilePath(txtExtensionFilter);

            // User has clicked cancel button
            if (path == null) {
                // Do not inform user, when he has clicked cancel button
                return;
            }

            saveConfiguration(path);
        } catch (IOException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
        }
    }

    /**
     * Handles statisticsFileLocation button.
     *
     * @param event ActionEvent.
     */
    private void statisticsFileLocationHandler(ActionEvent event) {
        if (saveStatistics.isSelected()) {
            String path = fileChooser.saveFilePath(csvExtensionFilter);

            // User has clicked cancel button
            if (path == null) {
                statisticsFileLocationStatus.setText("Nie podano lokalizacji.");
                return;
            }

            try {
                statisticsFileLocationURL = new PathValue(path);
                statisticsFileLocationStatus.setText("Zapisano lokalizację.");
            } catch (InvalidPathException e) {
                e.printStackTrace();
                statisticsFileLocationStatus.setText(e.getMessage());
            }
        } else {
            statisticsFileLocation.setDisable(true);
            statisticsFileLocationStatus.setText("");
        }
    }

    /**
     * Handles saveStatistics checkbox.
     *
     * @param event ActionEvent.
     */
    private void saveStatisticsHandler(ActionEvent event) {
        if (saveStatistics.isSelected()) {
            statisticsFileLocation.setDisable(false);
        } else {
            statisticsFileLocation.setDisable(true);
            statisticsFileLocationStatus.setText("");
            statisticsFileLocationURL = null;
        }
    }

    /**
     * Handles runSimulation button.
     *
     * @param event ActionEvent.
     */
    private void runSimulationHandler(ActionEvent event) {
        try {
            // Get all arguments
            Map<ConfigurationConstant, ISimulationConfigurationValue> args = getSimulationOptions();

            // Send arguments to simulation stage
            Platform.runLater(() -> {
                try {
                    new SimulationStage(args, new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                    alert(Alert.AlertType.ERROR, "Błąd pliku", "Error", e.getMessage());
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Błąd załączania symulacji", "Error", e.getMessage());
        }
    }

    // ************* Initialization *************

    /**
     * Loads example configurations from resources folder and initializes variables which handle form.
     *
     * @throws IOException if example configurations cannot be loaded.
     */
    private void loadProperties() throws IOException {
        // Load files from configurationsFolderPath directory
        String filePath = new File("").getAbsolutePath().concat(configurationsFolderPath);

        try (Stream<Path> stream = Files.list(Paths.get(filePath))) {
            exampleConfigurations = stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).sorted().map((fileName) -> {
                // Remove extension from file name
                int pos = fileName.lastIndexOf(".");
                if (pos > 0 && pos < (fileName.length() - 1)) {
                    fileName = fileName.substring(0, pos);
                }
                return fileName;
            }).toArray(String[]::new);
        } catch (IOException e) {
            throw new IOException(e);
        }

        // ********* TextField
        simulationProperties.put(ConfigurationConstant.MAP_WIDTH, new TextFieldHandler(mapWidth, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MAP_HEIGHT, new TextFieldHandler(mapHeight, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_START_NUMBER, new TextFieldHandler(animalStartNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.GENOTYPE_LENGTH, new TextFieldHandler(genomeLength, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_START_ENERGY, new TextFieldHandler(animalStartEnergy, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY, new TextFieldHandler(animalCreationEnergy, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST, new TextFieldHandler(animalCreationEnergyConsumption, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_START_NUMBER, new TextFieldHandler(plantStartNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_ENERGY, new TextFieldHandler(plantEnergy, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_SPAWN_NUMBER, new TextFieldHandler(plantSpawnNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MINIMUM_MUTATION_NUMBER, new TextFieldHandler(minimumMutationNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MAXIMUM_MUTATION_NUMBER, new TextFieldHandler(maximumMutationNumber, exampleConfiguration));

        // ********* ChoiceBox<String>
        simulationProperties.put(ConfigurationConstant.MAP_VARIANT, new ChoiceBoxHandler(mapVariant, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_BEHAVIOUR_VARIANT, new ChoiceBoxHandler(animalBehaviourVariant, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_GROWTH_VARIANT, new ChoiceBoxHandler(plantGrowVariant, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MUTATION_VARIANT, new ChoiceBoxHandler(mutationVariant, exampleConfiguration));
    }


    /**
     * Initializes ChoiceBox from List of strings.
     *
     * @param choiceBox ChoiceBox to initialize.
     * @param values    List of strings to initialize ChoiceBox.
     */
    private void initializeChoiceBox(ChoiceBox<ISimulationConfigurationValue> choiceBox, ISimulationConfigurationValue[] values) {
        choiceBox.getItems().addAll(values);
        choiceBox.getSelectionModel().selectFirst();
    }

    private void initializeChoiceBox(ChoiceBox<String> choiceBox, String[] values) {
        choiceBox.getItems().addAll(values);
        choiceBox.getSelectionModel().selectFirst();
    }


    /**
     * Initializes SimulationConfiguration handlers, listeners, variables.
     *
     * @param location  URL.
     * @param resources ResourceBundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadProperties();

            // Initialize ChoiceBox objects
            initializeChoiceBox(animalBehaviourVariant, AnimalBehaviourVariant.values());
            initializeChoiceBox(mapVariant, MapVariant.values());
            initializeChoiceBox(plantGrowVariant, PlantGrowthVariant.values());
            initializeChoiceBox(mutationVariant, MutationVariant.values());
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
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Błąd inicjalizacji", "Error", e.getMessage());
        }
    }


    // ************* Utils *************

    /**
     * Shows alert with given parameters.
     *
     * @param alertType Alert type.
     * @param title     Alert title.
     * @param header    Alert header.
     * @param content   Alert content.
     */
    private void alert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
