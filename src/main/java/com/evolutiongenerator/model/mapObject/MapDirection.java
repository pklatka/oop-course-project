package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST -> "W";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
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
            case NORTH_WEST -> new Vector2d(-1, 1);
            case SOUTH_EAST -> new Vector2d(1, -1);
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
