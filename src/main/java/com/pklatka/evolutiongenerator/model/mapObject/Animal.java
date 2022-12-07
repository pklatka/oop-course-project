package com.pklatka.evolutiongenerator.model.mapObject;

import com.pklatka.evolutiongenerator.model.map.IPositionChangeObserver;
import com.pklatka.evolutiongenerator.model.map.IWorldMap;
import com.pklatka.evolutiongenerator.model.map.RectangularMap;
import com.pklatka.evolutiongenerator.utils.Vector2d;

import java.util.ArrayList;
import java.util.Objects;

public class Animal implements IMapElement {
    private MapDirection heading = MapDirection.NORTH;
    private Vector2d position;
    private IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal() {
        this.map = new RectangularMap(4, 4);
        this.position = new Vector2d(2, 2);
    }

    public Animal(IWorldMap map) {
        this.map = map;
        this.position = new Vector2d(2, 2);
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        this.map = map;
        this.position = initialPosition;
    }

    @Override
    public String toString() {
        return switch (heading) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST -> "W";
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
        };
    }

    public String getImageResource() {
        return switch (heading) {
            case NORTH -> "head_north.png";
            case WEST -> "head_west.png";
            case SOUTH -> "head_south.png";
            case EAST -> "head_east.png";
        };
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

    public void move(MoveDirection direction) {
        // direction handling
        switch (direction) {
            case RIGHT -> heading = heading.next();
            case LEFT -> heading = heading.previous();
            case FORWARD, BACKWARD -> {
                // Simulate position change
                Vector2d oldPosition = new Vector2d(position.x, position.y);
                Vector2d unitVector = heading.toUnitVector();
                if (direction == MoveDirection.BACKWARD) {
                    unitVector = unitVector.opposite();
                }

                if (map.canMoveTo(oldPosition.add(unitVector))) {
                    position = position.add(unitVector);
                    positionChanged(oldPosition, position);
                }
            }
        }

    }
}