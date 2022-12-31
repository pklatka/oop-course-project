package com.evolutiongenerator.stage;

import com.evolutiongenerator.constant.AnimalStatistics;
import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.constant.SimulationStatistics;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.utils.Vector2d;

import java.util.List;
import java.util.Map;

/**
 * Interface for simulation observers, in our case stages of application.
 *
 * @author Patryk Klatka, Paweł Motyka
 */
public interface ISimulationObserver {

    /**
     * Render simulation statistics.
     *
     * @param essentialStatistics Statistics to show in GUI.
     */
    void renderMainStatistics(Map<SimulationStatistics, ISimulationConfigurationValue> essentialStatistics);

    /**
     * Add element to map (GridPane).
     *
     * @param mapElement Element to add.
     * @param position   Position of element.
     */
    boolean addElementToMap(IMapElement mapElement, Vector2d position, boolean selectMapElement);

    /**
     * Remove element from map (GridPane).
     *
     * @param mapElement Element to remove.
     */
    boolean removeElementFromMap(IMapElement mapElement);

    /**
     * Change element position on map (GridPane).
     * Note: you can only change position of the animal.
     *
     * @param mapElement  Element to change position.
     * @param newPosition New position of element.
     * @throws IllegalArgumentException If element is not an animal.
     */
    void changeElementPositionOnMap(IMapElement mapElement, Vector2d newPosition);

    /**
     * Adds genome to ListView with popular genomes.
     *
     * @param animal Animal which has genome with toString method.
     */
    void addGenomeToPopularGenomes(Animal animal);

    /**
     * Removes genome from ListView with popular genomes.
     *
     * @param animal Animal which has genome with toString method.
     */
    void removeGenomeFromPopularGenomes(Animal animal);

    /**
     * Updates animal statistics if animal was selected when simulation was paused.
     *
     * @param animalStatistics Animal statistics.
     */
    void updateAnimalStatistics(Map<AnimalStatistics, ISimulationConfigurationValue> animalStatistics);
}
