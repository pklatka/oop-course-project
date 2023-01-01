package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.AnimalBehaviourVariant;
import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.constant.MutationVariant;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Animal.Genes;
import com.evolutiongenerator.model.mapObject.MapDirection;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Vector2d;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTest {

    private static IWorldMap map;
    private static Genes genes;
    private static Animal animal;

    @BeforeAll
    public static void init() {
        map = new ForestedEquatorMap(20, 20, 20, MapVariant.GLOBE);
        genes = new Genes(10, 0, 0, MutationVariant.SLIGHT_CORRECTION, AnimalBehaviourVariant.NORMAL);
        animal = new Animal(map, new Vector2d(2, 2), genes, 200, 30, 50);
        map.place(animal);
    }

    @Test
    public void isInsideMapTest() {
        assertTrue(map.isInsideMap(animal.getPosition()));
    }


    @Test
    public void cleanDeadAnimalsTest() {
        List<Animal> list = map.cleanDeadAnimals();
        assertEquals(0, list.size());

        animal.decreaseEnergy(200);
        map.decreaseAnimalsEnergy();
        List<Animal> list2 = map.cleanDeadAnimals();
        assertEquals(1, list2.size());

    }

    @Test
    public void getReproduceConflictedPositionsTest() {
        Set<Vector2d> set = map.getReproduceConflictedPositions();
        assertEquals(0, set.size());

    }

    @Test
    public void getMapVariantTest() {
        assertEquals(MapVariant.GLOBE, map.getMapVariant());
        IWorldMap map2 = new ToxicCorpsesMap(20, 20, 20, MapVariant.INFERNAL_PORTAL);
        assertEquals(MapVariant.INFERNAL_PORTAL, map2.getMapVariant());
    }

    @Test
    public void generateRandomPositionTest(){
        Vector2d position = map.generateRandomPosition();
        assertTrue(map.isInsideMap(position));
    }



}
