package com.evolutiongenerator.stage;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
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
     * Render simulation grid.
     *
     * @param mapElements Map elements to render on grid.
     * @param essentialStatistics Statistics to show in GUI.
     */
    void renderGrid(Map<Vector2d, List<IMapElement>> mapElements, Map<ConfigurationConstant, ISimulationConfigurationValue> essentialStatistics);

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
     * @param animal Animal to show statistics.
     */
    void updateAnimalStatistics(Animal animal);
}
