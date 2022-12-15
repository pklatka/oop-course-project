package com.evolutiongenerator.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Mutation variants constants
 *
 * @author Patryk Klatka
 */
public enum MutationVariant implements ISimulationValue {
    /**
     * The gene is replaced with a random gene.
     * */
    RANDOM,
    /**
     * A mutation changes a gene by 1 up or down (e.g. gene 3 can be changed to 2 or 4 and gene 0 to 1 or 7).
     */
    SLIGHT_CORRECTION;

    /**
     * Returns a string representation of constant
     *
     * @author Patryk Klatka
     * @return String representation of constant
     */
    public String toString() {
        return switch (this) {
            case RANDOM -> "pełna losowość";
            case SLIGHT_CORRECTION -> "lekka korekta";
        };
    }

    public static ISimulationValue fromString(String value) throws IllegalArgumentException {
        return Arrays.stream(MutationVariant.values())
                .filter(v -> v.toString().equals(value))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("No enum constant " + value));
    }
}
