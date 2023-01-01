package com.evolutiongenerator.model.ui;

import com.evolutiongenerator.constant.ConfigurationConstant;
import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.constant.IntegerValue;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Plant;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiMapElementTest {

    @Test
    public void testGetAnimalEnergyColor(){
        Animal animal = new Animal(null, null, null, 100, 20,30);
        Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = new HashMap<>();
        simulationOptions.put(ConfigurationConstant.ANIMAL_START_ENERGY, new IntegerValue(100));

        GuiMapElement guiMapElement = new GuiMapElement(50, 50, animal, simulationOptions);

        assertEquals(guiMapElement.getAnimalEnergyColor(120).getHue(), 80.0);
        assertEquals(guiMapElement.getAnimalEnergyColor(20).getHue(), 0.5 * 20 + 30);
        assertEquals(guiMapElement.getAnimalEnergyColor(-5).getHue(), 0.0);
    }

    @Test
    public void testCreateMapElementRepresentationAnimal(){
        Animal animal = new Animal(null, null, null, 100, 20,30);
        Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = new HashMap<>();
        simulationOptions.put(ConfigurationConstant.ANIMAL_START_ENERGY, new IntegerValue(100));

        GuiMapElement guiMapElement = new GuiMapElement(50, 50, animal, simulationOptions);

        assertEquals(guiMapElement.getChildren().size(), 1);
        assertEquals(guiMapElement.getChildren().get(0).getClass(), Circle.class);
    }

    @Test
    public void testCreateMapElementRepresentationPlant(){
        Plant plant = new Plant(null, 100,false);
        Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = new HashMap<>();
        simulationOptions.put(ConfigurationConstant.ANIMAL_START_ENERGY, new IntegerValue(100));

        GuiMapElement guiMapElement = new GuiMapElement(50, 50, plant, simulationOptions);

        assertEquals(guiMapElement.getChildren().size(), 1);
        assertEquals(guiMapElement.getChildren().get(0).getClass(), Rectangle.class);
    }

    @Test
    public void testSelectMapElement(){
        Animal animal = new Animal(null, null, null, 100, 20,30);
        Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = new HashMap<>();
        simulationOptions.put(ConfigurationConstant.ANIMAL_START_ENERGY, new IntegerValue(100));

        GuiMapElement guiMapElement = new GuiMapElement(50, 50, animal, simulationOptions);
        guiMapElement.selectMapElement();
        assertEquals(guiMapElement.getChildren().size(), 1);
        assertEquals(guiMapElement.getChildren().get(0).getClass(), Circle.class);
        assertEquals(guiMapElement.getChildren().get(0).getStyle(), "-fx-stroke: #0051ff; -fx-stroke-width: 2px;");

        guiMapElement.unselectMapElement();
        assertEquals(guiMapElement.getChildren().get(0).getStyle(), "-fx-stroke: none; -fx-stroke-width: 0px;");
    }

    @Test
    public void testGetMapElement(){
        Animal animal = new Animal(null, null, null, 100, 20,30);
        Map<ConfigurationConstant, ISimulationConfigurationValue> simulationOptions = new HashMap<>();
        simulationOptions.put(ConfigurationConstant.ANIMAL_START_ENERGY, new IntegerValue(100));

        GuiMapElement guiMapElement = new GuiMapElement(50, 50, animal, simulationOptions);
        assertEquals(guiMapElement.getMapElement(), animal);
    }
}
