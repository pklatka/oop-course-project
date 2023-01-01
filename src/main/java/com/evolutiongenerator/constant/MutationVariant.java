package com.evolutiongenerator.constant;

import java.util.Arrays;

/**
 * Mutation variants constants.
 *
 * @author Patryk Klatka
 */
public enum MutationVariant implements ISimulationConfigurationValue {
    /**
     * The gene is replaced with a random gene.
     */
    RANDOM,
    /**
     * A mutation changes a gene by 1 up or down (e.g. gene 3 can be changed to 2 or 4 and gene 0 to 1 or 7).
     */
    SLIGHT_CORRECTION;

    /**
     * Returns a string representation of constant.
     *
     * @return String representation of constant.
     */
    public String toString() {
        return switch (this) {
            case RANDOM -> "pełna losowość";
            case SLIGHT_CORRECTION -> "lekka korekta";
        };
    }

    /**
     * Returns a parsed from string MutationVariant value.
     *
     * @param value String value.
     * @return ISimulationConfigurationValue value.
     */
    public static ISimulationConfigurationValue fromString(String value) throws IllegalArgumentException {
        return Arrays.stream(MutationVariant.values())
                .filter(v -> v.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak stałej " + MutationVariant.class.getCanonicalName() + "." + value));
    }
}
