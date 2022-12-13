package com.evolutiongenerator.constant;

import java.util.Arrays;

public enum MutationVariant
{
    RANDOM, // The gene is replaced with a random gene.
    SLIGHT_CORRECTION; // A mutation changes a gene by 1 up or down (e.g. gene 3 can be changed to 2 or 4 and gene 0 to 1 or 7).

    public String toString() {
        return switch (this) {
            case RANDOM -> "pełna losowość";
            case SLIGHT_CORRECTION -> "lekka korekta";
        };
    }

    public static MutationVariant fromString(String text) throws IllegalArgumentException{
        return Arrays.stream(MutationVariant.values())
                .filter(mutationVariant -> mutationVariant.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + MutationVariant.class.getCanonicalName() + "." + text));
    }

    public static String[] getValuesAsStringArray() {
        return Arrays.stream(MutationVariant.values()).map(Enum::toString).toArray(String[]::new);
    }
}
