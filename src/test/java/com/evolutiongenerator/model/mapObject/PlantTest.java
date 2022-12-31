package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.utils.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PlantTest {

    @Test
    public void testEquals(){
        Plant plant = new Plant(new Vector2d(2,4), 1);
        Plant plant2 = new Plant(new Vector2d(2,4), 1);
        Plant plant3 = new Plant(new Vector2d(5,4), 5);

        assertEquals(plant, plant2);
        assertNotEquals(plant, plant3);
    }

    @Test
    public void testGetPosition(){
        Plant plant = new Plant(new Vector2d(2,4), 1);
        assertEquals(plant.getPosition(), new Vector2d(2,4));
    }

    @Test
    public void testGetEnergy(){
        Plant plant = new Plant(new Vector2d(2,4), 1);
        assertEquals(plant.getEnergy(), 1);
    }
}
