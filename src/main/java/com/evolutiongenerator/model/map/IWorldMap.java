package com.evolutiongenerator.model.map;

import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Vector2d;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 */
public interface IWorldMap {
    HashMap<Vector2d,Integer> mapDeathStat = new HashMap<>();
    /**
     * Used to verify the correctness of positions
     *
     * @param position The position checked for the movement possibility.
     * @return information about whether the item is inside the map and does not cross the map boundaries
     */
    boolean isInsideMap(Vector2d position);

    /**
     *
     * @param position
     * @return  Plant from a given position. If there is none it returns null
     */
    Plant getPlantFrom(Vector2d position);

    /**
     *
     * @param position The position of the animals.
     * @return Animals from given position
     */
    TreeSet<Animal> getAnimalsFrom(Vector2d position);


    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);


    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Used to return a position suitable for a given map variant.
     * @param newPosition position to be replaced
     * @return Position on the map relative to the map variant.
     */
    Vector2d getRelativePositionToMapVariant(Vector2d newPosition);
    void cleanDeadAnimals();

    void growGrass();


}