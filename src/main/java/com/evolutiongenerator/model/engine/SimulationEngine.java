package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.model.map.ForestedEquatorMap;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.map.ToxicCorpsesMap;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Animal.Genes;
import com.evolutiongenerator.model.mapObject.MoveDirection;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.stage.ISimulationObserver;
import com.evolutiongenerator.stage.SimulationStageOld;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class SimulationEngine implements IEngine, Runnable {

    private MoveDirection[] directionArray;
    private IWorldMap map;
    // Use ArrayList to remember initial animal order
    private final ArrayList<Animal> animalsOrder = new ArrayList<>();
    private int moveDelay = 1000;
    private boolean isRunning = false;
    private boolean isPaused = true;
    private final List<ISimulationObserver> observers = new ArrayList<>();
    private final Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions;
    private Animal observedAnimal = null;
    private final Map<SimulationStatistics, ISimulationConfigurationValue> simulationStatistics;
    int totalDeadAnimals = 0;
    int totalSumOfAnimalLifespan = 0;
    int countPlants = 0;
    int mapWidth = 0;
    int mapHeight = 0;
    int day = 0;

    public SimulationEngine(Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions, ISimulationObserver observer) {
        this.simulationOptions = simulationOptions;
        this.observers.add(observer);

        PlantGrowthVariant mapPlantVariant = (PlantGrowthVariant) simulationOptions.get(ConfigurationConstant.PLANT_GROWTH_VARIANT);
        IntegerValue mapWidth = (IntegerValue) simulationOptions.get(ConfigurationConstant.MAP_WIDTH);
        this.mapWidth = mapWidth.getValue();
        IntegerValue mapHeight = (IntegerValue) simulationOptions.get(ConfigurationConstant.MAP_HEIGHT);
        this.mapHeight = mapHeight.getValue();
        MapVariant mapVariant = (MapVariant) simulationOptions.get(ConfigurationConstant.MAP_VARIANT);
        IntegerValue plantValue = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_ENERGY);


        IntegerValue genLength = (IntegerValue) simulationOptions.get(ConfigurationConstant.GENOTYPE_LENGTH);
        IntegerValue maximumMutationNumber = (IntegerValue) simulationOptions.get(ConfigurationConstant.MAXIMUM_MUTATION_NUMBER);
        IntegerValue reproduceCost = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST);
        IntegerValue minimalEnergyToReproduce = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY);
        IntegerValue minimumMutationNumber = (IntegerValue) simulationOptions.get(ConfigurationConstant.MINIMUM_MUTATION_NUMBER);
        IntegerValue startAnimalEnergy = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_START_ENERGY);
        IntegerValue initialAmountOfAnimals = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_START_NUMBER);
        MutationVariant mutationVariant = (MutationVariant) simulationOptions.get(ConfigurationConstant.MUTATION_VARIANT);
        AnimalBehaviourVariant behaviourVariant = (AnimalBehaviourVariant) simulationOptions.get(ConfigurationConstant.ANIMAL_BEHAVIOUR_VARIANT);

        if (mapPlantVariant == PlantGrowthVariant.FORESTED_EQUATOR) {
            this.map = new ForestedEquatorMap(mapWidth.getValue(), mapHeight.getValue(), plantValue.getValue(), mapVariant);
        }
        this.map = switch (mapPlantVariant) {
            case FORESTED_EQUATOR ->
                    new ForestedEquatorMap(mapWidth.getValue(), mapHeight.getValue(), plantValue.getValue(), mapVariant);
            case TOXIC_CORPSES ->
                    new ToxicCorpsesMap(mapWidth.getValue(), mapHeight.getValue(), plantValue.getValue(), mapVariant);
        };

        // Add animals to map
        for (int i = 0; i < initialAmountOfAnimals.getValue(); i++) {
            Genes genes = new Genes(genLength.getValue(), maximumMutationNumber.getValue(), minimumMutationNumber.getValue(), mutationVariant, behaviourVariant);
            Vector2d position = map.generateRandomPosition();
            Animal newAnimal = new Animal(map, position, genes, startAnimalEnergy.getValue(), reproduceCost.getValue(), minimalEnergyToReproduce.getValue());
            if (map.place(newAnimal)) {
                animalsOrder.add(newAnimal);
                observers.forEach(ob -> Platform.runLater(() -> ob.addElementToMap(newAnimal, newAnimal.getPosition())));
            }
        }

        // Add plants to map
        IntegerValue initialPlantsAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_START_NUMBER);

        for (int i = 0; i < initialPlantsAmount.getValue(); i++) {
            Plant plant = map.growPlant();
            observers.forEach(ob->{
                Platform.runLater(() ->ob.addElementToMap(plant, plant.getPosition()));
            });
        }

        countPlants = initialPlantsAmount.getValue();

        // Initialize simulation statistics
        simulationStatistics = new HashMap<>();
        simulationStatistics.put(SimulationStatistics.DAY, new IntegerValue(0));
        simulationStatistics.put(SimulationStatistics.NUMBER_OF_ANIMALS, new IntegerValue(initialAmountOfAnimals.getValue()));
        simulationStatistics.put(SimulationStatistics.NUMBER_OF_PLANTS, new IntegerValue(initialPlantsAmount.getValue()));
        simulationStatistics.put(SimulationStatistics.NUMBER_OF_EMPTY_FIELDS, new IntegerValue(mapWidth.getValue() * mapHeight.getValue() - initialPlantsAmount.getValue()));
        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_ENERGY, new IntegerValue(startAnimalEnergy.getValue()));
        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_LIFESPAN, new IntegerValue(0));

        Platform.runLater(() -> observers.forEach(ob -> ob.renderMainStatistics(simulationStatistics)));

        try{
            initializeStatisticsFile();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * @deprecated TODO: Remove in the future
     */
    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray) {
        // TODO handle map
        // TODO handle positionArray
        this.directionArray = directionArray;
        this.simulationStatistics = new HashMap<>();
        this.simulationOptions = new HashMap<>();
    }

    /**
     * @deprecated TODO: Remove in the future
     */
    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, ISimulationObserver observer) {
        this(map, positionArray, directionArray);
        this.observers.add(observer);
    }

    /**
     * @deprecated TODO: Remove in the future
     */
    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, ISimulationObserver observer, int moveDelay) {
        this(map, positionArray, directionArray, observer);
        this.moveDelay = moveDelay;
    }


    // Use it when you need to wait for thread
    private void runAndWait() throws InterruptedException {
        try {
            // Why using CountDownLatch?
            // Rendering grid might be time-consuming, so we must be ensured, that old grid has already been rendered.
            CountDownLatch doneLatch = new CountDownLatch(observers.size());
            for (ISimulationObserver simulationStageOld : observers) {
                Platform.runLater(() -> {
                    try {
//                        simulationStageOld.renderGrid();
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }
            doneLatch.await();
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            isRunning = true;
            while (isRunning) {
                if (isPaused) {
                    // Simulation is paused
                    Thread.sleep(300);
                } else {
                    // Simulation executes default procedure
                    IntegerValue plantSpawnAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_SPAWN_NUMBER);

                    List<Animal> animalsToRemove = map.cleanDeadAnimals();
                    animalsOrder.removeAll(animalsToRemove);
                    animalsToRemove.forEach(Animal::makeDead);

                    // Update average animal life span
                    totalDeadAnimals += animalsToRemove.size();
                    totalSumOfAnimalLifespan += animalsToRemove.stream().mapToInt(Animal::getDays).sum();
                    if(totalDeadAnimals != 0){
                        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_LIFESPAN, new DoubleValue((double) (totalSumOfAnimalLifespan/totalDeadAnimals)));
                    }

                    observers.forEach(observer -> {
                        Platform.runLater(() ->animalsToRemove.forEach(observer::removeElementFromMap));
                    });

                    for (Animal animal : animalsOrder) {
                        Platform.runLater(() -> {
                            for(ISimulationObserver observer: observers){
                                observer.removeElementFromMap(animal);
                            }
                            animal.move();
                            for(ISimulationObserver observer: observers){
                                observer.addElementToMap(animal, animal.getPosition());
                            }
                    });
                    }

                    // Update animal number
                    simulationStatistics.put(SimulationStatistics.NUMBER_OF_ANIMALS, new IntegerValue(animalsOrder.size()));

                    // Update average animal energy
                    if(animalsOrder.size() != 0){
                        int sumOfAnimalEnergy = animalsOrder.stream().mapToInt(Animal::getEnergy).sum();
                        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_ENERGY, new DoubleValue((double) (sumOfAnimalEnergy/animalsOrder.size())));
                    }

                    // Eat plants
                    Thread.sleep(200);
                    Set<Vector2d> plantsToConsume = map.getPlantToConsume();
                    for (Vector2d vector2d : plantsToConsume) {
                        TreeSet<Animal> animals = map.getAnimalsFrom(vector2d);
                        if (animals.size() > 1) {
                            Animal bestAnimal = map.resolveConflicts(vector2d, null);
                            Plant eatenPlant = bestAnimal.consume(map.getPlantFrom(vector2d));
                            countPlants--;
                            observers.forEach(observer -> {
                                Platform.runLater(() -> {
                                    observer.removeElementFromMap(eatenPlant);
                                });
                            });
                        } else if (animals.size() == 1) {
                            Animal animal = animals.descendingSet().first();
                            Plant eatenPlant = animal.consume(map.getPlantFrom(vector2d));
                            observers.forEach(observer -> {
                                Platform.runLater(() -> {
                                    observer.removeElementFromMap(eatenPlant);
                                });
                            });
                            countPlants--;
                        }
                    }

                    // Clear hashmap of plants to consume
                    map.cleanPlantsToConsume();

                    // Reproduce animals
                    Thread.sleep(200);
                    Set<Vector2d> positions = map.getReproduceConflictedPositions();

                    for (Vector2d position : positions) {
                        TreeSet<Animal> animals = map.getAnimalsFrom(position);
                        Animal bestAnimal = animals.descendingSet().first();
                        Animal partnerAnimal = map.resolveConflicts(position, bestAnimal);

                        if (partnerAnimal == null)
                            continue;

                        Animal offspringAnimal = bestAnimal.reproduce(partnerAnimal);

                        if (offspringAnimal != null) {
                            map.place(offspringAnimal);
                            observers.forEach(observer->{
                                Platform.runLater(() -> observer.addElementToMap(offspringAnimal, offspringAnimal.getPosition()));
                            });
                        }
                    }

                    map.clearReproduceConflictedPositions();

                    // Grow new plants
                    for (int i = 0; i < plantSpawnAmount.getValue(); i++) {
                        Plant plant = map.growPlant();
                        if (plant == null) continue;
                        observers.forEach(observer->{
                            Platform.runLater(() -> observer.addElementToMap(plant, plant.getPosition()));
                        });
                        countPlants++;
                    }

                    // Update plant number
                    simulationStatistics.put(SimulationStatistics.NUMBER_OF_PLANTS, new IntegerValue(countPlants));
                    simulationStatistics.put(SimulationStatistics.NUMBER_OF_EMPTY_FIELDS, new IntegerValue(mapWidth * mapHeight - countPlants - animalsOrder.size()));

                    // Update day number
                    simulationStatistics.put(SimulationStatistics.DAY, new IntegerValue(++day));

                    // Decrease energy
                    map.decreaseAnimalsEnergy();

                    // Write to statistics file
                    saveStatisticsToFile();

                    // Update statistics
                    Platform.runLater(() -> observers.forEach(ob -> ob.renderMainStatistics(simulationStatistics)));

                    // Update animal statistics
                    if(observedAnimal != null){
                        Map<AnimalStatistics, ISimulationConfigurationValue> animalStatistics = new HashMap<>();
                        animalStatistics.put(AnimalStatistics.ANIMAL_GENOME, new StringValue(observedAnimal.getGenome().toString()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_ACTIVE_GENOME, new IntegerValue(observedAnimal.getGenome().getCurrentGen()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_ENERGY, new IntegerValue(observedAnimal.getEnergy()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_NUMBER_OF_CHILDREN, new IntegerValue(observedAnimal.getChildrenAmount()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_LIFESPAN, new IntegerValue(observedAnimal.getDays()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_EATEN_PLANTS, new IntegerValue(observedAnimal.getEatenPlants()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_DEATH_DAY, new StringValue(observedAnimal.isAlive() ? "Alive" : "Death"));
                        Platform.runLater(() -> observers.forEach(ob -> ob.updateAnimalStatistics(animalStatistics)));
                    }

                    // Delay simulation
                    Thread.sleep(moveDelay);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Symulacja została przerwana.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    public void kill() {
        isRunning = false;
    }

    /**
     * Add ISimulationObserver.
     *
     * @param observer
     */
    @Override
    public void addObserver(ISimulationObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove ISimulationObserver.
     *
     * @param observer
     */
    @Override
    public void removeObserver(ISimulationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Select animal to observe its statistics.
     *
     * @param animal Animal to observe.
     */
    @Override
    public void selectAnimalToObserve(Animal animal) {
        observedAnimal = animal;
    }

    public void setDirectionArray(MoveDirection[] directionArray) {
        this.directionArray = directionArray;
    }


    private void initializeStatisticsFile() throws IOException {
        try {
            PathValue filePath = (PathValue) simulationOptions.get(ConfigurationConstant.STATISTICS_FILE_PATH);

            // If path is not specified - do not initialize file
            if(filePath == null){
                return;
            }

            ArrayList<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (SimulationStatistics statistic : SimulationStatistics.values()) {
                if(simulationStatistics.containsKey(statistic)){
                    line.append(statistic.toString()).append(',');
                }
            }
            line.deleteCharAt(line.length() - 1);
            lines.add(line.toString());

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath.getValue());
            Files.write(path, lines, utf8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch (InvalidPathException e){
            throw new IOException("Błędna ścieżka do pliku z zapisem danych", e);
        }
        catch (IOException | IllegalArgumentException e) {
            throw new IOException(e);
        }
    }

    public void saveStatisticsToFile() throws IOException {
        try {
            PathValue filePath = (PathValue) simulationOptions.get(ConfigurationConstant.STATISTICS_FILE_PATH);

            // If path is not specified - do not save statistics
            if(filePath == null){
                return;
            }

            ArrayList<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (SimulationStatistics statistic : SimulationStatistics.values()) {
                if(simulationStatistics.containsKey(statistic)){
                    line.append(simulationStatistics.get(statistic).toString()).append(',');
                }
            }
            line.deleteCharAt(line.length() - 1);
            lines.add(line.toString());

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath.getValue());
            Files.write(path, lines, utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch (InvalidPathException e){
            throw new IOException("Błędna ścieżka do pliku z zapisem danych", e);
        }
        catch (IOException | IllegalArgumentException e) {
            throw new IOException(e);
        }
    }
}