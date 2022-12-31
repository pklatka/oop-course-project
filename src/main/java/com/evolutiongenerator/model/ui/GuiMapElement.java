package com.evolutiongenerator.model.ui;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.constant.IntegerValue;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.Plant;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import com.evolutiongenerator.model.mapObject.Animal.Animal;

import java.util.Map;

/**
 * Class representing map element in GUI
 *
 * @author Patryk Klatka
 */
public class GuiMapElement extends StackPane {
    double width;
    double height;
    double widthPadding;
    double heightPadding;
    private IMapElement mapElement;
    private final Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions;

    /**
     * Constructor
     *
     * @param mapElement map element to be represented
     */
    public GuiMapElement(double width, double height, IMapElement mapElement, Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions) throws IllegalArgumentException {
        super();
        this.width = width;
        this.height = height;
        this.widthPadding = width / 10;
        this.heightPadding = height / 10;
        this.mapElement = mapElement;
        this.simulationOptions = simulationOptions;
        this.setAlignment(Pos.CENTER);

        try {
            createMapElementRepresentation();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Gets HSL color representing eneryg of animal
     */
    Color getAnimalEnergyColor(double percentage) {
        double saturation = 1.0;
        double brightness = 0.75;
        double hue;

        if (percentage > 100) {
            hue = 80.0;
        } else if (percentage > 0) {
            hue = 0.5 * percentage + 30; // Linear function from points (0, 30) and (100, 80)
        } else {
            hue = 0.0;
        }

        return Color.hsb(hue, saturation, brightness);
    }

    /**
     * Creates map element representation
     */
    private void createMapElementRepresentation() throws IllegalArgumentException {
        if (mapElement instanceof Animal animal) {
            // Create animal representation -> circle
            Circle circle = new Circle(Math.min(width / 2 - widthPadding, height / 2 - heightPadding));

            // Get energy color
            if (!simulationOptions.containsKey(ConfigurationConstant.ANIMAL_START_ENERGY)) {
                throw new IllegalArgumentException("Nie zadano startowej energii zwierzęcia.");
            }

            IntegerValue animalStartEnergy = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_START_ENERGY);
            Color animalEnergyColor = getAnimalEnergyColor((double) 100 * animal.getEnergy() / animalStartEnergy.getValue());

            // Set calculated color
            circle.setFill(animalEnergyColor);

            // Add circle to stack pane
            this.getChildren().add(circle);
        } else if (mapElement instanceof Plant) {
            Rectangle rectangle = new Rectangle(width, height);

            // Set plant color
            rectangle.setFill(Color.rgb(0, 94, 0));

            // Add rectangle to stack pane
            this.getChildren().add(rectangle);
        }
    }

    /**
     * Selects map element
     */
    public void selectMapElement() {
        if (this.getChildren().size() < 1) {
            return;
        }

        this.getChildren().get(0).setStyle("-fx-stroke: #0051ff; -fx-stroke-width: 2px;");
    }

    /**
     * Deselects map element
     */
    public void unselectMapElement() {
        if (this.getChildren().size() < 1) {
            return;
        }

        this.getChildren().get(0).setStyle("-fx-stroke: none; -fx-stroke-width: 0px;");
    }

    /**
     * Gets map element
     */
    public IMapElement getMapElement() {
        return mapElement;
    }
}
