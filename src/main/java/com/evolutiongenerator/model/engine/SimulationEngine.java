package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.constant.*;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Animal.Genes;
import com.evolutiongenerator.model.mapObject.MoveDirection;
import com.evolutiongenerator.stage.ISimulationObserver;
import com.evolutiongenerator.stage.SimulationStageOld;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SimulationEngine implements IEngine, Runnable {

    private MoveDirection[] directionArray;
    private IWorldMap map;
    // Use ArrayList to remember initial animal order
    private final ArrayList<Animal> animalsOrder = new ArrayList<>();
    private int moveDelay = 1000;
    private final ArrayList<ISimulationObserver> observers = new ArrayList<>();
    private Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = null; // TODO: Remove null and make variable final
    private Animal observedAnimal = null; // TODO: Remove null and make variable final
    public SimulationEngine(Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions){
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

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray) {
        this.map = map;
        IntegerValue genLength =  (IntegerValue) simulationOptions.get(ConfigurationConstant.GENOTYPE_LENGTH);
        IntegerValue maximumMutationNumber = (IntegerValue) simulationOptions.get(ConfigurationConstant.MAXIMUM_MUTATION_NUMBER);
        IntegerValue reproduceCost = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST);
        IntegerValue minimalEnergyToReproduce = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY);
        IntegerValue minimumMutationNumber = (IntegerValue) simulationOptions.get(ConfigurationConstant.MINIMUM_MUTATION_NUMBER);
        IntegerValue startAnimalEnergy = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_START_ENERGY);
        MutationVariant mutationVariant =  (MutationVariant) simulationOptions.get(ConfigurationConstant.MUTATION_VARIANT);
        AnimalBehaviourVariant behaviourVariant =  (AnimalBehaviourVariant) simulationOptions.get(ConfigurationConstant.ANIMAL_BEHAVIOUR_VARIANT);

        // Add animals to map
        for (Vector2d position : positionArray) {
            Genes genes = new Genes(genLength.getValue(),maximumMutationNumber.getValue(),minimumMutationNumber.getValue(), mutationVariant, behaviourVariant);
            Animal newAnimal = new Animal(map, position,genes, startAnimalEnergy.getValue(), reproduceCost.getValue(), minimalEnergyToReproduce.getValue());
            if (map.place(newAnimal)) {
                animalsOrder.add(newAnimal);
            }
        }

        // Add plants to map
        IntegerValue plantValue = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_ENERGY);
        IntegerValue plantSpawnAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_SPAWN_NUMBER);
        IntegerValue initialPlantsAmount = (IntegerValue) simulationOptions.get(ConfigurationConstant.PLANT_START_NUMBER);
        PlantGrowthVariant plantGrowthVariant = (PlantGrowthVariant) simulationOptions.get(ConfigurationConstant.PLANT_GROWTH_VARIANT);

        // TODO Implement maps and the sowPlants method in them. Call that method initialPlantAmount times

    }

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray) {
        this(map, positionArray);
        this.directionArray = directionArray;
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, ISimulationObserver observer) {
        this(map, positionArray, directionArray);
        this.observers.add(observer);
    }

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

    private void dispatchAnimation() {
        for (ISimulationObserver simulationStageOld : observers) {
//            Platform.runLater(simulationStageOld::renderGrid);
        }
    }

    @Override
    public void run() {
        int n = animalsOrder.size();
        if(observers.size() == 0) {
            System.out.println(map);
            for (Animal animal : animalsOrder) {
                animal.move();
                System.out.println(map);
            }
            return;
        }
        try {
            dispatchAnimation();
            for (Animal animal : animalsOrder) {
                animal.move();
                dispatchAnimation();
                Thread.sleep(moveDelay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// TODO CLEAR ?
        // Run default run function when no GUI observers
//        if (observers.size() == 0) {
//            System.out.println(map);
//            for (int i = 0; i < directionArray.length; i++) {
//                animalsOrder.get(i % n).move(directionArray[i]);
//                System.out.println(map);
//            }
//            return;
//        }
//
//        try {
//            dispatchAnimation();
//            for (int i = 0; i < directionArray.length; i++) {
//                animalsOrder.get(i % n).move(directionArray[i]);
//                dispatchAnimation();
//                Thread.sleep(moveDelay);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void pause() {

    }

    /**
     * Add ISimulationObserver.
     *
     * @param observer
     */
    @Override
    public void addObserver(ISimulationObserver observer) {

    }

    /**
     * Remove ISimulationObserver.
     *
     * @param observer
     */
    @Override
    public void removeObserver(ISimulationObserver observer) {

    }

    /**
     * Select animal to observe it's statistics.
     *
     * @param animal Animal to observe.
     */
    @Override
    public void selectAnimalToObserve(Animal animal) {

    }

    public void setDirectionArray(MoveDirection[] directionArray) {
        this.directionArray = directionArray;
    }

}