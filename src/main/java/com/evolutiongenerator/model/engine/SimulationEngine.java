package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.model.map.ForestedEquatorMap;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.map.ToxicCorpsesMap;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Animal.Genes;
import com.evolutiongenerator.model.mapObject.MoveDirection;
import com.evolutiongenerator.stage.ISimulationObserver;
import com.evolutiongenerator.stage.SimulationStageOld;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Platform;

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
    private Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = null; // TODO: Remove null and make variable final
    private Animal observedAnimal = null; // TODO: Remove null and make variable final

    public SimulationEngine(Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions) {
        this.simulationOptions = simulationOptions;

        // TODO: Generate map, animals, etc. according to simulationOptions
    }

    public SimulationEngine(Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions, int moveDelay) {
        this(simulationOptions);

        this.moveDelay = moveDelay;
    }

    public SimulationEngine(Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions, int moveDelay, ISimulationObserver observer) {
        this(simulationOptions, moveDelay);
        this.observers.add(observer);
    }

    public SimulationEngine() {
        PlantGrowthVariant mapPlantVariant = (PlantGrowthVariant) simulationOptions.get(ConfigurationConstant.PLANT_GROWTH_VARIANT);
        IntegerValue mapWidth = (IntegerValue) simulationOptions.get(ConfigurationConstant.MAP_WIDTH);
        IntegerValue mapHeight = (IntegerValue) simulationOptions.get(ConfigurationConstant.MAP_HEIGHT);
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
            }
        }

        // Add plants to map
        IntegerValue initialPlantsAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_START_NUMBER);

        for (int i = 0; i < initialPlantsAmount.getValue(); i++) {
            map.growPlant();
        }

    }

    /**
     * @deprecated TODO: Remove in the future
     */
    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray) {
        this();
        // TODO handle map
        // TODO handle positionArray
        this.directionArray = directionArray;
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
                    System.out.println("paused!");
                    Thread.sleep(300);
                } else {
                    // Simulation executes default procedure
                    System.out.println("running");
                    Thread.sleep(moveDelay);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Symulacja została przerwana.");
        }

        // TODO: Move this to try-catch above

        IntegerValue plantSpawnAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_SPAWN_NUMBER);
        map.cleanDeadAnimals();

        int n = animalsOrder.size();
        if (observers.size() == 0) {
            System.out.println(map);
            for (Animal animal : animalsOrder) {
                animal.move();
                System.out.println(map);
            }
            return;
        }
        try {
//            dispatchAnimation();
            for (Animal animal : animalsOrder) {
                animal.move();
//                dispatchAnimation();
                Thread.sleep(moveDelay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Eat plants
        Set<Vector2d> plantsToConsume = map.getPlantToConsume();

        for (Vector2d vector2d : plantsToConsume) {
            TreeSet<Animal> animals = map.getAnimalsFrom(vector2d);

            if (animals.size() > 1) {
                Animal bestAnimal = map.resolveConflicts(vector2d, null);
                bestAnimal.consume(map.getPlantFrom(vector2d));
            } else {
                Animal animal = animals.descendingSet().first();
                animal.consume(map.getPlantFrom(vector2d));
            }
        }
        // Reproduce animals
        ArrayList<Vector2d> positions = map.getReproduceConflictedPositions();

        for (Vector2d position : positions) {
            TreeSet<Animal> animals = map.getAnimalsFrom(position);
            Animal bestAnimal = animals.descendingSet().first();
            Animal partnerAnimal = map.resolveConflicts(position, bestAnimal);

            Animal offspringAnimal = bestAnimal.reproduce(partnerAnimal);

            if (offspringAnimal != null) {
                map.place(offspringAnimal);
            }
        }

        // Grow new plants
        for (int i = 0; i < plantSpawnAmount.getValue(); i++) {
            map.growPlant();
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

}