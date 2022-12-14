package com.evolutiongenerator.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Mutation variants constants
 *
 * @author Patryk Klatka
 */
public enum MutationVariant
{
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

    /**
     * Returns a constant representation of string
     *
     * @author Patryk Klatka
     * @throws IllegalArgumentException if string is not a valid constant
     * @return Constant, if representation exists
     */
    public static MutationVariant fromString(String text) throws IllegalArgumentException{
        return Arrays.stream(MutationVariant.values())
                .filter(mutationVariant -> mutationVariant.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + MutationVariant.class.getCanonicalName() + "." + text));
    }

    /**
     * Returns a string list of all constants
     *
     * @author Patryk Klatka
     * @return String list of all constants
     */
    public static List<String> getValuesAsStringList() {
        return Stream.of(MutationVariant.values()).map(Enum::toString).toList();
    }
}
