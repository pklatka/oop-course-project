package com.evolutiongenerator.constant;

/**
 * Enum for map object types.
 *
 * @author Patryk Klatka
 */
public enum MapObjectType {
    ANIMAL,
    GRASS,
    PLANT;

    public String toString() {
        return switch (this) {
            case ANIMAL -> "Animal";
            case GRASS -> "Grass";
            case PLANT -> "Plant";
        };
    }
}
