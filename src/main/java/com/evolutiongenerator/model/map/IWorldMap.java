package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 */
public interface IWorldMap {

    /**
     * Used to verify the correctness of positions
     *
     * @param position The position checked for the movement possibility.
     * @return information about whether the item is inside the map and does not cross the map boundaries
     */
    boolean isInsideMap(Vector2d position);

    /**
     * Used to return plants from a given position
     *
     * @param position Position from which we want to get the plant
     * @return Plant from a given position. If there is none it returns null
     */
    Plant getPlantFrom(Vector2d position);

    /**
     * Used to return animals from a given position
     *
     * @param position The position of the animals.
     * @return Animals from given position
     */
    TreeSet<Animal> getAnimalsFrom(Vector2d position);


    /**
     * Place an animal on the map.
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
     *
     * @param newPosition position to be replaced
     * @return Position on the map relative to the map variant.
     */
    Vector2d getRelativePositionToMapVariant(Vector2d newPosition);

    /**
     * Used to remove dead animals from the map and hashmap
     */
    void cleanDeadAnimals();

    /**
     * It is used to grow plants on the map on different variants of the map should be handled differently
     */
    void growGrass();

    /**
     * It is used to add an item on which there is an animal conflict that will need to be resolved.
     *
     * @param position Position on which the conflict occurred
     */
    void addConflictedPosition(Vector2d position);

    /**
     * Detection of conflicts arising
     *
     * @return Information on whether there was a conflict
     */
    boolean isConflictsOccurred();

    /**
     * @return a list of conflicts that have arisen
     */
    ArrayList<Vector2d> getConflictedPositions();

    /**
     * @return Returns a variant of the map
     */
    MapVariant getMapVariant();

    /**
     * @param newPosition new position that should be checked
     * @return Returns information on whether the animal should change its direction
     */
    boolean isAnimalChangingDirection(Vector2d newPosition);

}