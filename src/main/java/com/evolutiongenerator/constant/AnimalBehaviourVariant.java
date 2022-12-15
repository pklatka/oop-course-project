package com.evolutiongenerator.constant;

import java.util.Arrays;

/**
 * Animal behaviour variants constants
 *
 * @author Patryk Klatka
 */
public enum AnimalBehaviourVariant implements ISimulationValue {
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
     * Returns a parsed from string AnimalBehaviourVariant value
     *
     * @author Patryk Klatka
     * @return ISimulationValue value
     */
    public static ISimulationValue fromString(String value) throws IllegalArgumentException {
        return Arrays.stream(AnimalBehaviourVariant.values())
                .filter(v -> v.toString().equals(value))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("No enum constant " + value));
    }
}
