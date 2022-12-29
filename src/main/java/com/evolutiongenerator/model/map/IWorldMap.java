package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Vector2d;

import java.util.*;

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
    List<Animal> cleanDeadAnimals();

    /**
     * It is used to grow plants on the map on different variants of the map should be handled differently
     *
     * @return Plant that was grown on the map
     */
    Plant growPlant();

    /**
     * Used to remove a plant from a given position
     * @param position position from which the plant is to be removed
     */
    void removePlant(Vector2d position);

    /**
     * @param position to see if there is a plant
     * @return information about whether there is a plant in a given position
     */
    boolean isPlantAt(Vector2d position);

    /**
     * is used to record the locations on which the plant is to be consumed
     * @param position Position on which the plant will be eaten
     */
    void addPlantToConsume(Vector2d position);

    /**
     * is used to return a list of plants to eat
     * @return List with items of plants to eat
     */
    Set<Vector2d> getPlantToConsume();

    /**
     * It is used to add an item on which there is an animal conflict that will need to be resolved.
     *
     * @param position Position on which the conflict occurred
     */
    void addReproduceConflictedPosition(Vector2d position);

    /**
     * Detection of conflicts arising
     *
     * @return Information on whether there was a conflict
     */
    boolean isReproduceConflictsOccurred();

    /**
     * @return a list of conflicts that have arisen
     */
    ArrayList<Vector2d> getReproduceConflictedPositions();

    /**
     * @return Returns a variant of the map
     */
    MapVariant getMapVariant();

    /**
     * Used to generate a random position in the middle of the map
     * @return random Vector2D position inside the map
     */
    Vector2d generateRandomPosition();

    /**
     * @param newPosition new position that should be checked
     * @return Returns information on whether the animal should change its direction
     */
    boolean isAnimalChangingDirection(Vector2d newPosition);

    /**
     * Resolves conflict of priority to do surgery between animals
     *
     * @param position Position on which the conflict occurred
     * @param animalToIgnore Animal which we should ignore during resolving conflicts
     * @return The animal that has priority to eat the plant/reproduction
     */
    Animal resolveConflicts(Vector2d position, Animal animalToIgnore);

}