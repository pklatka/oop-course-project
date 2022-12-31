package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.model.map.ForestedEquatorMap;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.map.ToxicCorpsesMap;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Animal.Genes;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.stage.ISimulationObserver;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class responsible for simulation logic.
 *
 * @author Patryk Klatka, Paweł Motyka
 */
public class SimulationEngine implements IEngine, Runnable {

    private IWorldMap map;
    // Use ArrayList to remember initial animal order
    private final List<Animal> animalsOrder = new CopyOnWriteArrayList<>();
    private int moveDelay = 100;
    private boolean isRunning = true;
    private boolean isPaused = true;
    private final List<ISimulationObserver> observers = new ArrayList<>();
    private final Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions;
    private Animal observedAnimal = null;
    private final Map<SimulationStatistics, ISimulationConfigurationValue> simulationStatistics;
    int totalDeadAnimals = 0;
    int totalSumOfAnimalLifespan = 0;
    int mapWidth;
    int mapHeight;
    AtomicInteger countPlants = new AtomicInteger(0);
    AtomicInteger day = new AtomicInteger(0);

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
                observers.forEach(ob -> Platform.runLater(() -> ob.addElementToMap(newAnimal, newAnimal.getPosition(), false)));
            }
        }

        // Add plants to map
        IntegerValue initialPlantsAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_START_NUMBER);

        for (int i = 0; i < initialPlantsAmount.getValue(); i++) {
            Plant plant = map.growPlant();
            observers.forEach(ob -> {
                Platform.runLater(() -> ob.addElementToMap(plant, plant.getPosition(), false));
            });
        }

        countPlants.set(initialPlantsAmount.getValue());

        // Initialize simulation statistics
        simulationStatistics = new HashMap<>();
        simulationStatistics.put(SimulationStatistics.DAY, new IntegerValue(0));
        simulationStatistics.put(SimulationStatistics.NUMBER_OF_ANIMALS, new IntegerValue(initialAmountOfAnimals.getValue()));
        simulationStatistics.put(SimulationStatistics.NUMBER_OF_PLANTS, new IntegerValue(initialPlantsAmount.getValue()));
        simulationStatistics.put(SimulationStatistics.NUMBER_OF_EMPTY_FIELDS, new IntegerValue(mapWidth.getValue() * mapHeight.getValue() - initialPlantsAmount.getValue()));
        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_ENERGY, new IntegerValue(startAnimalEnergy.getValue()));
        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_LIFESPAN, new IntegerValue(0));

        Platform.runLater(() -> observers.forEach(ob -> ob.renderMainStatistics(simulationStatistics)));

        try {
            initializeStatisticsFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Main simulation procedure
     */
    @Override
    public void run() {
        try {
            while (isRunning) {
                if (isPaused) {
                    // Simulation is paused
                    Thread.sleep(300);
                } else {
                    // Simulation executes default procedure
                    IntegerValue plantSpawnAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_SPAWN_NUMBER);
                    List<Animal> animalsToRemove = map.cleanDeadAnimals();
                    animalsOrder.removeAll(animalsToRemove);

                    animalsToRemove.forEach((animal) -> {
                        animal.makeDead(day.get());
                    });


                    for (Animal animal : animalsOrder) {
                        animal.move();
                    }

                    // Eat plants
                    Set<Plant> plantsToRemove = ConcurrentHashMap.newKeySet();
                    Set<Plant> plantsToAdd = ConcurrentHashMap.newKeySet();

                    Set<Vector2d> plantsToConsume = map.getPlantToConsume();
                    for (Vector2d vector2d : plantsToConsume) {
                        TreeSet<Animal> animals = map.getAnimalsFrom(vector2d);
                        if (animals.size() > 1) {
                            Animal bestAnimal = map.resolveConflicts(vector2d, null);
                            if(bestAnimal == null){
                                continue;
                            }
                            Plant eatenPlant = bestAnimal.consume(map.getPlantFrom(vector2d));

                            if (eatenPlant.isOnEquator()) {
                                ((ForestedEquatorMap) map).decreaseEquatorPlantAmount();
                            }

                            countPlants.decrementAndGet();
                            plantsToRemove.add(eatenPlant);
                        } else if (animals.size() == 1) {
                            Animal animal = animals.descendingSet().first();
                            if(animal == null){
                                continue;
                            }
                            Plant eatenPlant = animal.consume(map.getPlantFrom(vector2d));

                            if (eatenPlant.isOnEquator()) {
                                ((ForestedEquatorMap) map).decreaseEquatorPlantAmount();
                            }

                            countPlants.decrementAndGet();
                            plantsToRemove.add(eatenPlant);
                        }
                    }

                    // Clear hashmap of plants to consume
                    map.cleanPlantsToConsume();

                    // Reproduce animals
                    Set<Vector2d> positions = map.getReproduceConflictedPositions();

                    for (Vector2d position : positions) {
                        TreeSet<Animal> animals = map.getAnimalsFrom(position);
                        Animal bestAnimal = animals.descendingSet().first();
                        Animal partnerAnimal = map.resolveConflicts(position, bestAnimal);

                        if (partnerAnimal == null) {
                            continue;
                        }

                        Animal offspringAnimal = bestAnimal.reproduce(partnerAnimal);

                        if (offspringAnimal != null) {
                            map.place(offspringAnimal);
                            animalsOrder.add(offspringAnimal);
                        }
                    }

                    map.clearReproduceConflictedPositions();

                    // Grow new plants
                    for (int i = 0; i < plantSpawnAmount.getValue(); i++) {
                        Plant plant = map.growPlant();
                        if (plant == null) continue;
                        plantsToAdd.add(plant);
                        countPlants.incrementAndGet();
                    }

                    // Decrease energy
                    map.decreaseAnimalsEnergy();

                    // Run GUI methods
                    // Note: Why we are using booleans instead of throwing exception?
                    // try-catch block is time-consuming, so we want to avoid it.
                    // especially when we run animation with 100ms delay.
                    Platform.runLater(() -> {
                            // Remove dead animals
                            for(ISimulationObserver observer : observers){
                                for(Animal animalToRemove : animalsToRemove) {
                                    boolean result = observer.removeElementFromMap(animalToRemove);
                                    if(!result){
                                        System.out.println("Zwierzę do usunięcia nie zostało usunięte.");
                                    }
                                }
                            }

                            for (Animal animal : animalsOrder) {
                                for (ISimulationObserver observer : observers) {
                                    // Remove current animals
                                    boolean removeResult = observer.removeElementFromMap(animal);
                                    if(!removeResult){
                                        System.out.println("Zwierzę do usunięcia nie zostało usunięte.");
                                    }
                                    boolean addResult = observer.addElementToMap(animal, animal.getPosition(), animal == observedAnimal);
                                    if(!addResult){
                                        System.out.println("Zwierzę do dodania nie zostało dodane.");
                                    }
                                }
                            }

                            for( ISimulationObserver observer : observers){
                                for(Plant plantToRemove : plantsToRemove){
                                    boolean result = observer.removeElementFromMap(plantToRemove);
                                    if(!result){
                                        System.out.println("Roślina do usunięcia nie została usunięta.");
                                    }
                                }
                            }

                            for( ISimulationObserver observer : observers){
                                for(Plant plantToAdd : plantsToAdd){
                                    boolean result = observer.addElementToMap(plantToAdd, plantToAdd.getPosition(), false);
                                    if(!result){
                                        System.out.println("Roślina do dodania nie została dodana.");
                                    }
                                }
                            }
                    });

                    // ******** Update statistics ********

                    // Update average animal life span
                    totalDeadAnimals += animalsToRemove.size();
                    totalSumOfAnimalLifespan += animalsToRemove.stream().mapToInt(Animal::getDays).sum();
                    if (totalDeadAnimals != 0) {
                        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_LIFESPAN, new DoubleValue((double) (totalSumOfAnimalLifespan / totalDeadAnimals)));
                    }

                    // Update animal number
                    simulationStatistics.put(SimulationStatistics.NUMBER_OF_ANIMALS, new IntegerValue(animalsOrder.size()));

                    // Update average animal energy
                    if (animalsOrder.size() != 0) {
                        int sumOfAnimalEnergy = animalsOrder.stream().mapToInt(Animal::getEnergy).sum();
                        simulationStatistics.put(SimulationStatistics.AVERAGE_ANIMAL_ENERGY, new DoubleValue((double) (sumOfAnimalEnergy / animalsOrder.size())));
                    }

                    // Update plant number
                    simulationStatistics.put(SimulationStatistics.NUMBER_OF_PLANTS, new IntegerValue(countPlants.get()));
                    simulationStatistics.put(SimulationStatistics.NUMBER_OF_EMPTY_FIELDS, new IntegerValue(mapWidth * mapHeight - countPlants.get()));

                    // Update day number
                    simulationStatistics.put(SimulationStatistics.DAY, new IntegerValue(day.incrementAndGet()));

                    // Write to statistics file
                    saveStatisticsToFile();

                    // Update statistics
                    Platform.runLater(() -> observers.forEach(ob -> ob.renderMainStatistics(simulationStatistics)));

                    // Update animal statistics
                    if (observedAnimal != null) {
                        Map<AnimalStatistics, ISimulationConfigurationValue> animalStatistics = new HashMap<>();
                        animalStatistics.put(AnimalStatistics.ANIMAL_GENOME, new StringValue(observedAnimal.getGenome().toString()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_ACTIVE_GENOME, new IntegerValue(observedAnimal.getGenome().getCurrentGen()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_ENERGY, new IntegerValue(observedAnimal.getEnergy()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_NUMBER_OF_CHILDREN, new IntegerValue(observedAnimal.getChildrenAmount()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_LIFESPAN, new IntegerValue(observedAnimal.getDays()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_EATEN_PLANTS, new IntegerValue(observedAnimal.getEatenPlantsAmount()));
                        animalStatistics.put(AnimalStatistics.ANIMAL_DEATH_DAY, new StringValue(observedAnimal.isAlive() ? "b. d." : ((Integer) observedAnimal.getDeathDay()).toString()));
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
        Platform.runLater(() -> observers.forEach(ob -> {
            if (observedAnimal != null) {
                ob.removeElementFromMap(observedAnimal);
                if (observedAnimal.isAlive()) {
                    ob.addElementToMap(observedAnimal, observedAnimal.getPosition(), false);
                }
            }
            observedAnimal = animal;
        }));
    }

    private void initializeStatisticsFile() throws IOException {
        try {
            PathValue filePath = (PathValue) simulationOptions.get(ConfigurationConstant.STATISTICS_FILE_PATH);

            // If path is not specified - do not initialize file
            if (filePath == null) {
                return;
            }

            ArrayList<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (SimulationStatistics statistic : SimulationStatistics.values()) {
                if (simulationStatistics.containsKey(statistic)) {
                    line.append(statistic.toString()).append(',');
                }
            }
            line.deleteCharAt(line.length() - 1);
            lines.add(line.toString());

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath.getValue());
            Files.write(path, lines, utf8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (InvalidPathException e) {
            throw new IOException("Błędna ścieżka do pliku z zapisem danych", e);
        } catch (IOException | IllegalArgumentException e) {
            throw new IOException(e);
        }
    }

    public void saveStatisticsToFile() throws IOException {
        try {
            PathValue filePath = (PathValue) simulationOptions.get(ConfigurationConstant.STATISTICS_FILE_PATH);

            // If path is not specified - do not save statistics
            if (filePath == null) {
                return;
            }

            ArrayList<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (SimulationStatistics statistic : SimulationStatistics.values()) {
                if (simulationStatistics.containsKey(statistic)) {
                    line.append(simulationStatistics.get(statistic).toString()).append(',');
                }
            }
            line.deleteCharAt(line.length() - 1);
            lines.add(line.toString());

            Charset utf8 = StandardCharsets.UTF_8;
            Path path = Paths.get(filePath.getValue());
            Files.write(path, lines, utf8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (InvalidPathException e) {
            throw new IOException("Błędna ścieżka do pliku z zapisem danych", e);
        } catch (IOException | IllegalArgumentException e) {
            throw new IOException(e);
        }
    }
}