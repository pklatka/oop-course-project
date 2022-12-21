package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.constant.IntegerValue;
import com.evolutiongenerator.constant.MutationVariant;
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
        IntegerValue minimumMutationNumber = (IntegerValue) simulationOptions.get(ConfigurationConstant.MINIMUM_MUTATION_NUMBER);
        MutationVariant mutationVariant =  (MutationVariant) simulationOptions.get(ConfigurationConstant.MUTATION_VARIANT);

        // TODO create Gen here for each animal and pass it to constructor
        // Add animals to map
        for (Vector2d position : positionArray) {
            Genes genes = new Genes(genLength.getValue(),maximumMutationNumber.getValue(),minimumMutationNumber.getValue(), mutationVariant);
            Animal newAnimal = new Animal(map, position,genes);
            if (map.place(newAnimal)) {
                animalsOrder.add(newAnimal);
            }

        }
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

        // Run default run function when no GUI observers
        if (observers.size() == 0) {
            System.out.println(map);
            for (int i = 0; i < directionArray.length; i++) {
                animalsOrder.get(i % n).move(directionArray[i]);
                System.out.println(map);
            }
            return;
        }

        try {
            dispatchAnimation();
            for (int i = 0; i < directionArray.length; i++) {
                animalsOrder.get(i % n).move(directionArray[i]);
                dispatchAnimation();
                Thread.sleep(moveDelay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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