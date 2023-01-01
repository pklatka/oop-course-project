package com.evolutiongenerator.model.mapObject.Animal;

import com.evolutiongenerator.constant.AnimalBehaviourVariant;
import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.constant.MutationVariant;
import com.evolutiongenerator.model.map.ForestedEquatorMap;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.mapObject.MapDirection;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Vector2d;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private static IWorldMap map;
    private static Genes genes;
    private static Animal animal;

    @BeforeAll
    public static void init() {
         map = new ForestedEquatorMap(20,20,20, MapVariant.GLOBE);
         genes = new Genes(10,0,0, MutationVariant.SLIGHT_CORRECTION, AnimalBehaviourVariant.NORMAL);
         animal = new Animal(map,new Vector2d(2,2),genes,200,30,50);
    }

    @Test
    public void testToString(){
        assertEquals(animal.getHeading().toString()  + " " + animal.getEnergy() + " " + animal.getPosition() + " DNI " + animal.getDays() + " dzieci " + animal.getChildrenAmount(),animal.toString());
    }

    @Test
    public void testMove(){
        int currentGen = genes.getCurrentGen();
        MapDirection direction = animal.changeDirection(currentGen);
        Vector2d beforeMovePosition = animal.getPosition();
        animal.move();
        assertEquals(beforeMovePosition.add(direction.toUnitVector()),animal.getPosition());
    }

    @Test
    public void testIsAlive(){
        assertTrue(animal.isAlive());
        animal.makeDead(1);
        assertFalse(animal.isAlive());
    }

    @Test
    public void testGetDeathDay(){
        assertEquals(-1,animal.getDeathDay());
        animal.makeDead(2);
        assertEquals(2,animal.getDeathDay());
    }


    @Test
    public void testIsAt(){
        assertTrue(animal.isAt(new Vector2d(2,2)));
    }

    @Test
    public void testReproduce(){
        Animal partner = new Animal(map,new Vector2d(2,2),genes,200,30,50);
        Animal child = animal.reproduce(partner);
        assertInstanceOf(Animal.class,child);
    }

    @Test
    public void testGetEatenDayAmount(){
        assertTrue(animal.isAt(new Vector2d(2,2)));
    }


    @Test
    public void testConsume(){
        assertNull(animal.consume(null));
        Plant plant = new Plant(new Vector2d(2,2),30,false);
        assertEquals(plant  ,animal.consume(plant));
    }

    @Test
    public void testGetEnergy(){
        assertEquals(200,animal.getEnergy());
        Plant plant = new Plant(new Vector2d(2,2),30,false);
        animal.consume(plant);
        assertEquals(230,animal.getEnergy());

    }


}
