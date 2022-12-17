package com.evolutiongenerator.constant;

import java.util.Arrays;

/**
 * Plant growth variants constants.
 * <p>
 * Note: When it comes to plant growth, certain fields are strongly preferred, according to the Pareto principle.
 * There is an 80% chance that a new plant will grow in a preferred field,
 * and only a 20% chance that it will grow in a second-class field.
 * About 20% of all places on the map are preferred, 80% of places are considered unattractive.
 *
 * @author Patryk Klatka
 */
public enum PlantGrowthVariant implements ISimulationConfigurationValue {
    /**
     * The plants' preference is for a horizontal strip of fields in the central part of the map
     * (pretending to be the equator and surrounding areas).
     */
    FORESTED_EQUATOR,
    /**
     * Plants prefer those fields where animals die the least - they grow in those
     * fields where the fewest animals ended their lives during the simulation.
     */
    TOXIC_CORPSES;

    /**
     * Returns a string representation of constant
     *
     * @return String representation of constant
     */
    public String toString() {
        return switch (this) {
            case FORESTED_EQUATOR -> "zalesione równiki";
            case TOXIC_CORPSES -> "toksyczne trupy";
        };
    }

    /**
     * Returns a parsed from string PlantGrowthVariant value
     *
     * @return ISimulationConfigurationValue value
     */
    public static ISimulationConfigurationValue fromString(String value) throws IllegalArgumentException {
        return Arrays.stream(PlantGrowthVariant.values())
                .filter(v -> v.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak stałej " + PlantGrowthVariant.class.getCanonicalName() + "." + value));
    }
}
