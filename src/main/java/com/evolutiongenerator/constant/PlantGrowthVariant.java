package com.evolutiongenerator.constant;

/*
* When it comes to plant growth, certain fields are strongly preferred, according to the Pareto principle.
* There is an 80% chance that a new plant will grow in a preferred field,
* and only a 20% chance that it will grow in a second-class field.
* About 20% of all places on the map are preferred, 80% of places are considered unattractive.
 * */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Plant growth variants constants
 *
 * @author Patryk Klatka
 */
public enum PlantGrowthVariant implements ISimulationValue {
    /**
     * The plants' preference is for a horizontal strip of fields in the central part of the map
     * (pretending to be the equator and surrounding areas).
     * */
    FORESTED_EQUATOR,
    /**
     * Plants prefer those fields where animals die the least - they grow in those
     * fields where the fewest animals ended their lives during the simulation.
     * */
    TOXIC_CORPSES;

    /**
     * Returns a string representation of constant
     *
     * @author Patryk Klatka
     * @return String representation of constant
     */
    public String toString() {
        return switch (this) {
            case FORESTED_EQUATOR -> "zalesione równiki";
            case TOXIC_CORPSES -> "toksyczne trupy";
        };
    }

    public static ISimulationValue fromString(String value) throws IllegalArgumentException {
        return Arrays.stream(PlantGrowthVariant.values())
                .filter(v -> v.toString().equals(value))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("No enum constant " + value));
    }
}
