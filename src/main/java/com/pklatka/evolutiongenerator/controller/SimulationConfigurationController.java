package com.pklatka.evolutiongenerator.controller;

import com.pklatka.evolutiongenerator.handler.ChoiceBoxHandler;
import com.pklatka.evolutiongenerator.handler.IConfigurationField;
import com.pklatka.evolutiongenerator.handler.TextFieldHandler;
import com.pklatka.evolutiongenerator.stage.SimulationStage;
import com.pklatka.evolutiongenerator.utils.FileChooser;
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
    @FXML
    private Label statisticsFileLocationStatus;
    private String statisticsFileLocationURL = "";
    private boolean stopListeningExampleConfiguration = false;

    // ************* Utils
    private final HashMap<String, IConfigurationField> simulationProperties = new HashMap<>();

    private String configurationsFolderPath = "/src/main/resources/simulation/configurations/";
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


        // Load elements to hashmap
        // ********* TextField
        simulationProperties.put("mapWidth", new TextFieldHandler(mapWidth, exampleConfiguration));
        simulationProperties.put("mapHeight", new TextFieldHandler(mapHeight, exampleConfiguration));
        simulationProperties.put("animalStartNumber", new TextFieldHandler(animalStartNumber, exampleConfiguration));
        simulationProperties.put("genomLength", new TextFieldHandler(genomLength, exampleConfiguration));
        simulationProperties.put("animalStartEnergy", new TextFieldHandler(animalStartEnergy, exampleConfiguration));
        simulationProperties.put("animalCreationEnergy", new TextFieldHandler(animalCreationEnergy, exampleConfiguration));
        simulationProperties.put("animalCreationEnergyConsumption", new TextFieldHandler(animalCreationEnergyConsumption, exampleConfiguration));
        simulationProperties.put("plantStartNumber", new TextFieldHandler(plantStartNumber, exampleConfiguration));
        simulationProperties.put("plantEnergy", new TextFieldHandler(plantEnergy, exampleConfiguration));
        simulationProperties.put("plantSpawnNumber", new TextFieldHandler(plantSpawnNumber, exampleConfiguration));
        simulationProperties.put("minimumMutationNumber", new TextFieldHandler(minimumMutationNumber, exampleConfiguration));
        simulationProperties.put("maximumMutationNumber", new TextFieldHandler(maximumMutationNumber, exampleConfiguration));

        // ********* ChoiceBox<String>
        simulationProperties.put("mapVariant", new ChoiceBoxHandler(mapVariant, exampleConfiguration));
        simulationProperties.put("animalBehaviourVariant", new ChoiceBoxHandler(animalBehaviourVariant, exampleConfiguration));
        simulationProperties.put("plantGrowVariant", new ChoiceBoxHandler(plantGrowVariant, exampleConfiguration));
        simulationProperties.put("mutationVariant", new ChoiceBoxHandler(mutationVariant, exampleConfiguration));
    }

    private HashMap<String, String> getSimulationOptions(){
        HashMap<String, String> args = new HashMap<>();
        // ******* Additional arguments
        args.put("statisticsFileLocationURL", statisticsFileLocationURL);

        for(String key: simulationProperties.keySet()) {
            String property = simulationProperties.get(key).readProperty();
            if(property.equals("")){
                alertError("Błąd parametru", "Error", "Parametr "+key+ " ma błędną wartość");
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
            return new File("").getAbsolutePath().concat(configurationsFolderPath + filename + ".txt");
    }
    private void loadConfiguration(String filePath) throws IllegalArgumentException, IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach((line) -> {
                // Parse line and update values in GUI
                if (line.contains("=")){
                    String[] propPair = line.split("=");
                    if(propPair.length < 2){
                        throw new IllegalArgumentException("Zły argument "+ propPair[0] +" w pliku " + filePath);
                    }
                    IConfigurationField field = simulationProperties.get(propPair[0].trim());
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
            for(String key: simulationProperties.keySet()){
                String property = simulationProperties.get(key).readProperty();
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
            String defaultConfigurationFilename = exampleConfiguration.getValue();
            loadConfiguration(getExampleConfigurationPath(defaultConfigurationFilename));

            stopListeningExampleConfiguration = true;
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
                HashMap<String, String> args = getSimulationOptions();

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
