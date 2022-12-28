package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

import java.util.Random;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH,SOUTH_WEST, WEST , NORTH_WEST;

    public String toString() {
        return switch (this) {
            case NORTH -> "NORTH";
            case SOUTH -> "SOUTH";
            case EAST -> "EAST";
            case WEST -> "WEST";
            case NORTH_EAST -> "NORTH_EAST";
            case NORTH_WEST -> "NORTH_WEST";
            case SOUTH_EAST -> "SOUTH_EAST";
            case SOUTH_WEST -> "SOUTH_WEST";
        };
    }

    public MapDirection next() {
        return MapDirection.values()[(this.ordinal() + 1) % MapDirection.values().length];
    }

    public MapDirection previous() {
        int length = MapDirection.values().length;
        return MapDirection.values()[(length + this.ordinal() - 1) % length];
    }

    public static MapDirection getRandomDirection() {
        MapDirection[] mapValues = values();
        int randomIndex = Randomize.generateInt(mapValues.length - 1, 0);
        return mapValues[randomIndex];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case WEST -> new Vector2d(-1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case EAST -> new Vector2d(1, 0);
            case NORTH_EAST -> new Vector2d(1, 1);
            case NORTH_WEST -> new Vector2d(1, -1);
            case SOUTH_EAST -> new Vector2d(-1, 1);
            default -> new Vector2d(-1, -1);
        };
    }

    public MapDirection getOppositeDirection() {
        return switch (this) {
            case NORTH -> MapDirection.SOUTH;
            case WEST -> MapDirection.EAST;
            case SOUTH -> MapDirection.NORTH;
            case EAST -> MapDirection.WEST;
            case NORTH_EAST -> MapDirection.SOUTH_WEST;
            case NORTH_WEST -> MapDirection.SOUTH_EAST;
            case SOUTH_EAST -> MapDirection.NORTH_WEST;
            default -> MapDirection.NORTH_EAST;
        };
    }
}
