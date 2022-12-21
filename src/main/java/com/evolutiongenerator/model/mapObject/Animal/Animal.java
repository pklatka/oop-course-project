package com.evolutiongenerator.model.mapObject.Animal;

import com.evolutiongenerator.constant.MutationVariant;
import com.evolutiongenerator.model.map.IPositionChangeObserver;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.map.RectangularMap;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.MapDirection;
import com.evolutiongenerator.model.mapObject.MoveDirection;
import com.evolutiongenerator.utils.Vector2d;

import java.util.ArrayList;
import java.util.Objects;

public class Animal implements IMapElement {
    private MapDirection heading = MapDirection.getRandomDirection();
    private Vector2d position;
    private IWorldMap map;
    private int energy;
    private Genes genes;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IWorldMap map) {
        this.map = map;
        this.position = new Vector2d(2, 2);
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Genes genes) {
        this.map = map;
        this.position = initialPosition;
        this.genes = genes;
    }

    @Override
    public String toString() {
        return switch (heading){
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST-> "W";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
        };
    }

    // Changing equals and hashCode methods to use HashMap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(position, animal.position) && Objects.equals(map, animal.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, map);
    }

    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    public MapDirection getHeading() {
        return switch (heading) {
            case NORTH -> MapDirection.NORTH;
            case WEST -> MapDirection.WEST;
            case SOUTH -> MapDirection.SOUTH;
            case EAST -> MapDirection.EAST;
            case NORTH_EAST -> MapDirection.NORTH_EAST;
            case NORTH_WEST -> MapDirection.NORTH_WEST;
            case SOUTH_EAST -> MapDirection.SOUTH_EAST;
            case SOUTH_WEST -> MapDirection.SOUTH_WEST;
        };
    }

    public String getImageResource() {
        return switch (heading) {
            case NORTH -> "head_north.png";
            case WEST -> "head_west.png";
            case SOUTH -> "head_south.png";
            case EAST -> "head_east.png";
            case NORTH_EAST -> "head_north_east.png";
            case NORTH_WEST -> "head_north_west.png";
            case SOUTH_EAST -> "head_south_east.png";
            case SOUTH_WEST -> "head_south_west.png";
        };
    }

    public void changeDirection(int gen){
        for (int i = 0; i < gen; i++)
            heading = heading.next();
    }

    public String getObjectLabel() {
        return this.toString() + ' ' + this.position.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.x == position.x && this.position.y == position.y;
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }

    public void move() {
        int currentGen = genes.getGen();
        changeDirection(currentGen);
        // TODO go forward after changingDirection
        // TODO handle variations
        // TODO notify observers about changing position
        // TODO change position on map

        // direction handling
//        switch (direction) {
//            case RIGHT -> heading = heading.next();
//            case LEFT -> heading = heading.previous();
//            case FORWARD, BACKWARD -> {
//                // Simulate position change
//                Vector2d oldPosition = new Vector2d(position.x, position.y);
//                Vector2d unitVector = heading.toUnitVector();
//                if (direction == MoveDirection.BACKWARD) {
//                    unitVector = unitVector.opposite();
//                }
//
//                if (map.canMoveTo(oldPosition.add(unitVector))) {
//                    position = position.add(unitVector);
//                    positionChanged(oldPosition, position);
//                }
//            }
//        }
    }

}