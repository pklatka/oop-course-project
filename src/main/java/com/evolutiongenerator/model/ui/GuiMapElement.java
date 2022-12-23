package com.evolutiongenerator.model.ui;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.constant.IntegerValue;
import com.evolutiongenerator.model.mapObject.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
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
    public GuiMapElement(double width, double height, IMapElement mapElement, Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions) {
        super();
        this.width = width;
        this.height = height;
        this.widthPadding = width / 10;
        this.heightPadding = height / 10;
        this.mapElement = mapElement;
        this.simulationOptions = simulationOptions;
        this.setAlignment(Pos.CENTER);

        createMapElementRepresentation();
    }

    /**
     * Gets HSL color representing eneryg of animal
     */
    private Color getAnimalEnergyColor(double percentage) {
        double saturation = 1.0;
        double brightness = 0.75;
        double hue;

        if (percentage > 100) {
            hue = 80.0;
        } else if (percentage > 0) {
            hue = 0.5*percentage + 30; // Linear function from points (0, 30) and (100, 80)
        }else{
            hue = 0.0;
        }

        return Color.hsb(hue, saturation, brightness);
    }

    /**
     * Creates map element representation
     */
    private void createMapElementRepresentation(){
        switch (mapElement.getObjectType()) {
            case ANIMAL -> {
                Animal animal = (Animal) mapElement;

                // Create animal representation -> circle
                Circle circle = new Circle(Math.min(width/2 - widthPadding, height/2 - heightPadding));

                // Get energy color
                IntegerValue animalStartEnergy = (IntegerValue) simulationOptions.get(ConfigurationConstant.ANIMAL_START_ENERGY);
                Color animalEnergyColor = getAnimalEnergyColor((double) 100 * animal.getEnergy() / animalStartEnergy.getValue());

                // Set calculated color
                circle.setFill(animalEnergyColor);

                // Add circle to stack pane
                this.getChildren().add(circle);
            }
            case PLANT -> {
                Rectangle rectangle = new Rectangle(width, height);

                // Set plant color
                rectangle.setFill(Color.rgb(0, 94, 0));

                // Add rectangle to stack pane
                this.getChildren().add(rectangle);
            }
        }
    }
}
