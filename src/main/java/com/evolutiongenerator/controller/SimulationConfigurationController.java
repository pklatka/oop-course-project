package com.evolutiongenerator.controller;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.handler.ChoiceBoxHandler;
import com.evolutiongenerator.handler.IConfigurationField;
import com.evolutiongenerator.handler.TextFieldHandler;
import com.evolutiongenerator.stage.SimulationStage;
import com.evolutiongenerator.utils.FileChooser;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationConfigurationController implements Initializable {

    // ********** Configuration for ChoiceBox objects
    List<String> exampleConfigurations;
    String[] animalBehaviourVariants = AnimalBehaviourVariant.getValuesAsStringArray();
    String[] mutationVariants = MutationVariant.getValuesAsStringArray();
    String[] plantGrowVariants = PlantGrowthVariant.getValuesAsStringArray();
    String[] mapVariants = MapVariant.getValuesAsStringArray();

    // ********** Configuration fields
    @FXML
    private Button runSimulation;
    @FXML
    private Button saveConfiguration;
    @FXML
    private Button loadConfiguration;
    @FXML
    private ChoiceBox<String> exampleConfiguration;

    // ********** Map fields
    @FXML
    private TextField mapWidth;
    @FXML
    private TextField mapHeight;
    @FXML
    private ChoiceBox<String> mapVariant;

    // ********** Animal fields
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

    // ********** Plants fields
    @FXML
    private TextField plantStartNumber;
    @FXML
    private TextField plantEnergy;
    @FXML
    private TextField plantSpawnNumber;
    @FXML
    private ChoiceBox<String> plantGrowVariant;

    // ********** Mutations fields
    @FXML
    private TextField minimumMutationNumber;
    @FXML
    private TextField maximumMutationNumber;
    @FXML
    private ChoiceBox<String> mutationVariant;

    // ********** Options fields
    @FXML
    private CheckBox saveStatistics;
    @FXML
    private Button statisticsFileLocation;
    @FXML
    private Label statisticsFileLocationStatus;
    private String statisticsFileLocationURL = "";
    private boolean stopListeningExampleConfiguration = false;

    // ************* Utils
    private final HashMap<ConfigurationConstant, IConfigurationField> simulationProperties = new HashMap<>();
    private final String configurationsFolderPath = "/src/main/resources/simulation/configurations/";
    private FileChooser fileChooser;

    private void loadProperties() throws IOException{
        // Load files from configurationsFolderPath directory
        String filePath = new File("").getAbsolutePath().concat(configurationsFolderPath);

        try (Stream<Path> stream = Files.list(Paths.get(filePath))) {
            exampleConfigurations = stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map((fileName) -> {
                        // Remove extension from file name
                        int pos = fileName.lastIndexOf(".");
                        if (pos > 0 && pos < (fileName.length() - 1)) {
                            fileName = fileName.substring(0, pos);
                        }
                        return fileName;
                    })
                    .collect(Collectors.toList());
        }catch (IOException e){
            e.printStackTrace();
            throw new IOException(e);
        }

        // ********* TextField
        simulationProperties.put(ConfigurationConstant.MAP_WIDTH, new TextFieldHandler(mapWidth, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MAP_HEIGHT, new TextFieldHandler(mapHeight, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_START_NUMBER, new TextFieldHandler(animalStartNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.GENOTYPE_LENGTH, new TextFieldHandler(genomLength, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_START_ENERGY, new TextFieldHandler(animalStartEnergy, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY, new TextFieldHandler(animalCreationEnergy, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST, new TextFieldHandler(animalCreationEnergyConsumption, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_START_NUMBER, new TextFieldHandler(plantStartNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_ENERGY, new TextFieldHandler(plantEnergy, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.PLANT_SPAWN_NUMBER, new TextFieldHandler(plantSpawnNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MINIMUM_MUTATION_NUMBER, new TextFieldHandler(minimumMutationNumber, exampleConfiguration));
        simulationProperties.put(ConfigurationConstant.MAXIMUM_MUTATION_NUMBER, new TextFieldHandler(maximumMutationNumber, exampleConfiguration));

        // ********* ChoiceBox<String>
        simulationProperties.put(ConfigurationConstant.MAP_VARIANT, new ChoiceBoxHandler(mapVariant, exampleConfiguration, MapVariant::fromString));
        simulationProperties.put(ConfigurationConstant.ANIMAL_BEHAVIOUR_VARIANT, new ChoiceBoxHandler(animalBehaviourVariant, exampleConfiguration, AnimalBehaviourVariant::fromString));
        simulationProperties.put(ConfigurationConstant.PLANT_GROWTH_VARIANT, new ChoiceBoxHandler(plantGrowVariant, exampleConfiguration, PlantGrowthVariant::fromString));
        simulationProperties.put(ConfigurationConstant.MUTATION_VARIANT, new ChoiceBoxHandler(mutationVariant, exampleConfiguration, MutationVariant::fromString));
    }

    private HashMap<ConfigurationConstant, Object> getSimulationOptions(){
        HashMap<ConfigurationConstant, Object> args = new HashMap<>();
        // ******* Additional arguments
        args.put(ConfigurationConstant.STATISTICS_FILE_PATH, statisticsFileLocationURL);

        for(ConfigurationConstant key: simulationProperties.keySet()) {
            Object property = simulationProperties.get(key).readProperty();
            if(property.equals("")){
                alertError("Błąd parametru", "Error", "Parametr "+ key+ " ma błędną wartość");
                return null;
            }
            args.put(key, property);
        }
        return args;
    }

    public void setFileChooserUtil(FileChooser fileChooser){
        this.fileChooser = fileChooser;
    }

    private String getExampleConfigurationPath(String filename){
        if(filename == null || filename.equals("")){
            return "";
        }

        return new File("").getAbsolutePath().concat(configurationsFolderPath + filename + ".txt");
    }
    private void loadConfiguration(String filePath) throws IllegalArgumentException, IOException {
        if(filePath.equals("")){
            return;
        }

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach((line) -> {
                // Parse line and update values in GUI
                if (line.contains("=")){
                    String[] propPair = line.split("=");
                    if(propPair.length < 2){
                        throw new IllegalArgumentException("Zły argument "+ propPair[0] +" w pliku " + filePath);
                    }
                    IConfigurationField field = simulationProperties.get(ConfigurationConstant.valueOf(propPair[0].trim()));
                    if (field != null){
                        if(propPair[1].equals("")){
                            throw new IllegalArgumentException("Parametr "+ propPair[0]+ " ma błędną wartość w pliku "+filePath);
                        }
                        field.writeProperty(propPair[1].trim());
                    }else {
                        throw new IllegalArgumentException("Zły argument "+ propPair[0] +" w pliku " + filePath);
                    }
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
            alertError("Odczyt pliku", "Error", "Błąd odczytu pliku: "+e.getLocalizedMessage());
            throw new IOException(e);
        }
    }


    private void saveConfiguration(String filePath) throws IOException {
        try {
            ArrayList<String> lines = new ArrayList<>();

            // Read data
            for(ConfigurationConstant key: simulationProperties.keySet()){
                Object property = simulationProperties.get(key).readProperty();
                if(property.equals("")){
                    alertError("Błąd parametru", "Error", "Parametr "+key+ " ma błędną wartość");
                    return;
                }
                lines.add(key + "=" + property);
            }

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath);
            Files.write(path, lines, utf8,
                    StandardOpenOption.CREATE);

            alertInformation("Zapis pliku", "Informacja", "Plik został zapisany");
        } catch (IOException e) {
            e.printStackTrace();
            alertError("Zapis pliku", "Error", "Błąd zapisu pliku: "+e.getLocalizedMessage());
            throw new IOException(e);
        }
    }

    private void alertError(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    private void alertInformation(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            loadProperties();

            // Initialize ChoiceBox objects
            animalBehaviourVariant.getItems().addAll(animalBehaviourVariants);
            if(animalBehaviourVariant.getItems().size() > 0){
                animalBehaviourVariant.setValue(animalBehaviourVariant.getItems().get(0));
            }

            mapVariant.getItems().addAll(mapVariants);
            if(mapVariant.getItems().size() > 0){
                mapVariant.setValue(mapVariant.getItems().get(0));
            }

            plantGrowVariant.getItems().addAll(plantGrowVariants);
            if(plantGrowVariant.getItems().size() > 0){
                plantGrowVariant.setValue(plantGrowVariant.getItems().get(0));
            }

            mutationVariant.getItems().addAll(mutationVariants);
            if(mutationVariant.getItems().size() > 0){
                mutationVariant.setValue(mutationVariant.getItems().get(0));
            }

            exampleConfiguration.getItems().addAll(exampleConfigurations);
            if(exampleConfiguration.getItems().size() > 0){
                exampleConfiguration.setValue(exampleConfiguration.getItems().get(0));
            }

            // Load default configuration
            String defaultConfigurationFilename = exampleConfiguration.getValue();
            loadConfiguration(getExampleConfigurationPath(defaultConfigurationFilename));
            exampleConfiguration.setValue(defaultConfigurationFilename);

            exampleConfiguration.setOnAction((event)->{
                try{
                    if(stopListeningExampleConfiguration){
                        stopListeningExampleConfiguration = false;
                        return;
                    }
                    if(exampleConfiguration.getValue().equals("")){
                        return;
                    }
                    String filename = exampleConfiguration.getValue();
                    loadConfiguration(getExampleConfigurationPath(filename));

                    stopListeningExampleConfiguration = true;
                    exampleConfiguration.setValue(filename);
                }catch (IOException | IllegalArgumentException e){
                    alertError("Błąd pliku", "Error", e.getMessage());
                }
            });

            // Initialize button handlers
            loadConfiguration.setOnAction((event) -> {
                try{
                    String path = fileChooser.getFilePath();
                    // User has clicked cancel button
                    if(path.equals("")) {
                        return;
                    }
                    loadConfiguration(path);
                    exampleConfiguration.setValue("");
                }catch (IOException | IllegalArgumentException e){
                    alertError("Błąd pliku","Error", e.getMessage());
                }
            });

            saveConfiguration.setOnAction((event)->{
                try{
                    String path = fileChooser.saveFilePath();
                    // User has clicked cancel button
                    if(path.equals("")) {
                        return;
                    }
                    saveConfiguration(path);
                }catch (IOException e){
                    alertError("Błąd pliku","Error", e.getMessage());
                }
            });

            statisticsFileLocation.setOnAction((event) -> {
                if(saveStatistics.isSelected()){
                    statisticsFileLocationURL = fileChooser.saveFilePath();
                    if (statisticsFileLocationURL.equals("")){
                        statisticsFileLocationStatus.setText("Nie podano lokalizacji!");
                    }else{
                        statisticsFileLocationStatus.setText("Zapisano lokalizację");
                    }
                }else{
                    statisticsFileLocation.setDisable(true);
                    statisticsFileLocationStatus.setText("");
                }
            });

            saveStatistics.setOnAction((event -> {
                if(saveStatistics.isSelected()){
                    statisticsFileLocation.setDisable(false);
                }else{
                    statisticsFileLocation.setDisable(true);
                    statisticsFileLocationStatus.setText("");
                    statisticsFileLocationURL = "";
                }
            }));

            runSimulation.setOnAction((event -> {
                // Get all arguments
                HashMap<ConfigurationConstant, Object> args = getSimulationOptions();

                if(args == null){
                    return;
                }

                // Send arguments to simulation stage
                new SimulationStage(new String[]{}, new Stage());
            }));
        }catch (IOException | IllegalArgumentException e){
            alertError("Błąd pliku","Error", e.getMessage());
        }
    }
}
