package com.evolutiongenerator.constant;

/**
 * Enum for map object types.
 *
 * @author Patryk Klatka
 */
public enum MapObjectType {
    ANIMAL,
    PLANT;

    public String toString() {
        return switch (this) {
            case ANIMAL -> "Animal";
            case PLANT -> "Plant";
        };
    }
}
