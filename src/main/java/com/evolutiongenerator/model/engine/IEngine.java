package com.evolutiongenerator.model.engine;

import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.stage.ISimulationObserver;
/**
 * Interface for simulation engine which is responsible for initializing and running simulation.
 *
 * @author Patryk Klatka, Paweł Motyka
 */
public interface IEngine {
    /**
     * Pause simulation thread.
     */
    void pause();

    /**
     * Resume simulation thread.
     */
    void resume();

    /**
     * Stop simulation thread.
     */
    void kill();

    /**
     * Add ISimulationObserver.
     *
     * @param observer Observer to add.
     */
    void addObserver(ISimulationObserver observer);

    /**
     * Remove ISimulationObserver.
     *
     * @param observer Observer to remove.
     */
    void removeObserver(ISimulationObserver observer);

    /**
     * Select animal to observe its statistics.
     *
     * @param animal Animal to observe. If null then no animal is selected.
     */
    void selectAnimalToObserve(Animal animal);
}
