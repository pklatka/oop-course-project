package com.evolutiongenerator.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Animal behaviour variants constants
 *
 * @author Patryk Klatka
 */
public enum AnimalBehaviourVariant {
    /**
     * The animal always performs the genes sequentially, one after the other
     * */
    NORMAL,

    /**
     * In 80% of the cases, the animal activates the gene immediately following the gene,
     * but in 20% of the cases it jumps to another random gene.
     */
    A_BIT_OF_MADNESS;

    /**
     * Returns a string representation of constant
     *
     * @author Patryk Klatka
     * @return String representation of constant
     */
    public String toString() {
        return switch (this) {
            case NORMAL -> "pełna predestynacja";
            case A_BIT_OF_MADNESS -> "nieco szaleństwa";
        };
    }

    /**
     * Returns a constant representation of string
     *
     * @author Patryk Klatka
     * @throws IllegalArgumentException if string is not a valid constant
     * @return Constant, if representation exists
     */
    public static AnimalBehaviourVariant fromString(String text) throws IllegalArgumentException{
        return Arrays.stream(AnimalBehaviourVariant.values())
                .filter(behaviourVariant -> behaviourVariant.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + AnimalBehaviourVariant.class.getCanonicalName() + "." + text));
    }

    /**
     * Returns a string list of all constants
     *
     * @author Patryk Klatka
     * @return String list of all constants
     */
    public static List<String> getValuesAsStringList() {
        return Stream.of(AnimalBehaviourVariant.values()).map(Enum::toString).toList();
    }
}
