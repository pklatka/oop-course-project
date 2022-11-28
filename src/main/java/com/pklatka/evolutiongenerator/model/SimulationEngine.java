package com.pklatka.evolutiongenerator.model;

import com.pklatka.evolutiongenerator.gui.App;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SimulationEngine implements IEngine, Runnable {

    private MoveDirection[] directionArray;
    private IWorldMap map;
    // Use ArrayList to remember initial animal order
    private final ArrayList<Animal> animalsOrder = new ArrayList<>();
    private int moveDelay = 1000;
    private final ArrayList<App> observers = new ArrayList<>();

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray) {
        this.map = map;

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

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, App observer) {
        this(map, positionArray, directionArray);
        this.observers.add(observer);
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positionArray, MoveDirection[] directionArray, App observer, int moveDelay) {
        this(map, positionArray, directionArray, observer);
        this.moveDelay = moveDelay;
    }


    // Use it when you need to wait for thread
    private void runAndWait() throws InterruptedException {
        try {
            // Why using CountDownLatch?
            // Rendering grid might be time-consuming, so we must be ensured, that old grid has already been rendered.
            CountDownLatch doneLatch = new CountDownLatch(observers.size());
            for (App app : observers) {
                Platform.runLater(() -> {
                    try {
                        app.renderGrid();
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
        for (App app : observers) {
            Platform.runLater(app::renderGrid);
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


    public void setDirectionArray(MoveDirection[] directionArray) {
        this.directionArray = directionArray;
    }

}