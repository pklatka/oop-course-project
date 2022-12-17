package com.evolutiongenerator.constant;

import java.util.Arrays;

/**
 * Map variants constants
 *
 * @author Patryk Klatka
 */
public enum MapVariant implements ISimulationConfigurationValue {
    /**
     * The left and right edges of the map loop (if the animal goes beyond the left edge,
     * it will appear on the right side - and if it goes beyond the right edge, it will appear on the left);
     * the top and bottom edges of the map are poles - you can't go there
     * (if the animal tries to go beyond these edges of the map, it stays on the field it was on,
     * and its direction changes to the opposite).
     */
    GLOBE,
    /**
     * If the pet goes beyond the edge of the map, it enters a magical portal;
     * its energy is reduced by a certain value (the same as for the generation of a descendant),
     * and it is then teleported to a new, random location on the map.
     */
    INFERNAL_PORTAL;

    /**
     * Returns a string representation of constant
     *
     * @return String representation of constant
     */
    public String toString() {
        return switch (this) {
            case GLOBE -> "kula ziemska";
            case INFERNAL_PORTAL -> "piekielny portal";
        };
    }

    /**
     * Returns a parsed from string MapVariant value
     *
     * @return ISimulationConfigurationValue value
     */
    public static ISimulationConfigurationValue fromString(String value) throws IllegalArgumentException {
        return Arrays.stream(MapVariant.values())
                .filter(v -> v.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak stałej " + MapVariant.class.getCanonicalName() + "." + value));
    }
}
