package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.MoveDirection;
import com.evolutiongenerator.stage.SimulationStageOld;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SimulationEngine implements IEngine, Runnable {

    private MoveDirection[] directionArray;
    private IWorldMap map;
    // Use ArrayList to remember initial animal order
    private final ArrayList<Animal> animalsOrder = new ArrayList<>();
    private int moveDelay = 1000;
    private final ArrayList<SimulationStageOld> observers = new ArrayList<>();

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray) {
        this.map = map;

        // TODO create Gen here for each animal and pass it to constructor
        // Add animals to map
        for (Vector2d position : positionArray) {
            Animal newAnimal = new Animal(map, position);
            if (map.place(newAnimal)) {
                animalsOrder.add(newAnimal);
            }

        }
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray) {
        this(map, positionArray);
        this.directionArray = directionArray;
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, SimulationStageOld observer) {
        this(map, positionArray, directionArray);
        this.observers.add(observer);
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, SimulationStageOld observer, int moveDelay) {
        this(map, positionArray, directionArray, observer);
        this.moveDelay = moveDelay;
    }


    // Use it when you need to wait for thread
    private void runAndWait() throws InterruptedException {
        try {
            // Why using CountDownLatch?
            // Rendering grid might be time-consuming, so we must be ensured, that old grid has already been rendered.
            CountDownLatch doneLatch = new CountDownLatch(observers.size());
            for (SimulationStageOld simulationStageOld : observers) {
                Platform.runLater(() -> {
                    try {
                        simulationStageOld.renderGrid();
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
        for (SimulationStageOld simulationStageOld : observers) {
            Platform.runLater(simulationStageOld::renderGrid);
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

    @Override
    public void resume() {

    }

    public void setDirectionArray(MoveDirection[] directionArray) {
        this.directionArray = directionArray;
    }

}