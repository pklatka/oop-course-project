package com.pklatka.evolutiongenerator.controller;

import com.pklatka.evolutiongenerator.handler.ChoiceBoxHandler;
import com.pklatka.evolutiongenerator.handler.IConfigurationField;
import com.pklatka.evolutiongenerator.handler.TextFieldHandler;
import com.pklatka.evolutiongenerator.utils.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class SimulationConfigurationController implements Initializable {

    // ********** Configuration for ChoiceBox objects
    String[] exampleConfigurations = new String[]{"configuration1", "file2"};
    String[] animalBehaviourVariants = new String[]{"pełna predestynacja", "nieco szaleństwa"};
    String[] mutationVariants = new String[]{"pełna losowość", "lekka korekta"};
    String[] plantGrowVariants = new String[]{"zalesione równiki", "toksyczne trupy"};
    String[] mapVariants = new String[]{"kula ziemska", "piekielny portal"};
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
    private final HashMap<String, IConfigurationField> simulationProperties = new HashMap<>();

    private FileChooserUtil fileChooserUtil;

    private void loadProperties(){
        // Load elements to hashmap
        // ********* TextField
        simulationProperties.put("mapWidth", new TextFieldHandler(mapWidth));
        simulationProperties.put("mapHeight", new TextFieldHandler(mapHeight));
        simulationProperties.put("animalStartNumber", new TextFieldHandler(animalStartNumber));
        simulationProperties.put("genomLength", new TextFieldHandler(genomLength));
        simulationProperties.put("animalStartEnergy", new TextFieldHandler(animalStartEnergy));
        simulationProperties.put("animalCreationEnergy", new TextFieldHandler(animalCreationEnergy));
        simulationProperties.put("animalCreationEnergyConsumption", new TextFieldHandler(animalCreationEnergyConsumption));
        simulationProperties.put("plantStartNumber", new TextFieldHandler(plantStartNumber));
        simulationProperties.put("plantEnergy", new TextFieldHandler(plantEnergy));
        simulationProperties.put("plantSpawnNumber", new TextFieldHandler(plantSpawnNumber));
        simulationProperties.put("minimumMutationNumber", new TextFieldHandler(minimumMutationNumber));
        simulationProperties.put("maximumMutationNumber", new TextFieldHandler(maximumMutationNumber));
        // ********* ChoiceBox<String>
        simulationProperties.put("exampleConfiguration", new ChoiceBoxHandler(exampleConfiguration));
        simulationProperties.put("mapVariant", new ChoiceBoxHandler(mapVariant));
        simulationProperties.put("animalBehaviourVariant", new ChoiceBoxHandler(animalBehaviourVariant));
        simulationProperties.put("plantGrowVariant", new ChoiceBoxHandler(plantGrowVariant));
        simulationProperties.put("mutationVariant", new ChoiceBoxHandler(mutationVariant));
    }

    public void setFileChooserUtil(FileChooserUtil fileChooserUtil){
        this.fileChooserUtil = fileChooserUtil;
    }

    private String getExampleConfigurationPath(String filename){
            String filePath = new File("").getAbsolutePath();
            return filePath.concat("/src/main/resources/example-configurations/" + filename + ".txt");
    }
    private void loadExampleConfiguration(String filePath){
        try{
            try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                stream.forEach((line) -> {
                    // Parse line and update values in GUI
                    if (line.contains("=")){
                        String[] propPair = line.split("=");
                        IConfigurationField field = simulationProperties.get(propPair[0].trim());
                        if (field != null){
                            field.writeProperty(propPair[1].trim());
                        }else {
                            // TODO: What if file is wrong?
                        }
                    }
                });
            }catch (NoSuchFileException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void saveConfiguration(String filePath){
        try {
            ArrayList<String> lines = new ArrayList<>();
            // Read data
            simulationProperties.keySet().forEach((key)->{
                IConfigurationField field = simulationProperties.get(key);
                lines.add(key + "=" + field.readProperty());
            });

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath);
            Files.write(path, lines, utf8,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProperties();

        // Initialize ChoiceBox objects
        animalBehaviourVariant.getItems().addAll(animalBehaviourVariants);
        animalBehaviourVariant.setValue(animalBehaviourVariant.getItems().get(0));

        mapVariant.getItems().addAll(mapVariants);
        mapVariant.setValue(mapVariant.getItems().get(0));

        plantGrowVariant.getItems().addAll(plantGrowVariants);
        plantGrowVariant.setValue(plantGrowVariant.getItems().get(0));

        mutationVariant.getItems().addAll(mutationVariants);
        mutationVariant.setValue(mutationVariant.getItems().get(0));

        exampleConfiguration.getItems().addAll(exampleConfigurations);
        exampleConfiguration.setValue(exampleConfiguration.getItems().get(0));

        // Load default configuration
        loadExampleConfiguration(getExampleConfigurationPath(exampleConfiguration.getValue()));

        exampleConfiguration.setOnAction((event)->{
            loadExampleConfiguration(getExampleConfigurationPath(exampleConfiguration.getValue()));
        });

        // Initialize button handlers
        loadConfiguration.setOnAction((event) -> {
            String path = fileChooserUtil.getFilePath();
            loadExampleConfiguration(path);
        });

        saveConfiguration.setOnAction((event)->{
            String path = fileChooserUtil.saveFilePath();
            saveConfiguration(path);
        });

        saveStatistics.setOnAction((event -> {}));

        runSimulation.setOnAction((event -> {}));
    }
}
