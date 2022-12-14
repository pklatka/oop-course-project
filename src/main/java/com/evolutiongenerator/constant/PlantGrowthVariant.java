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
public enum PlantGrowthVariant {
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

    /**
     * Returns a constant representation of string
     *
     * @author Patryk Klatka
     * @throws IllegalArgumentException if string is not a valid constant
     * @return Constant, if representation exists
     */
    public static Object fromString(String text) throws IllegalArgumentException{
        return Arrays.stream(PlantGrowthVariant.values())
                .filter(plantGrowthVariant -> plantGrowthVariant.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + PlantGrowthVariant.class.getCanonicalName() + "." + text));
    }

    /**
     * Returns a string list of all constants
     *
     * @author Patryk Klatka
     * @return String list of all constants
     */
    public static List<String> getValuesAsStringList() {
        return Stream.of(PlantGrowthVariant.values()).map(Enum::toString).toList();
    }
}
