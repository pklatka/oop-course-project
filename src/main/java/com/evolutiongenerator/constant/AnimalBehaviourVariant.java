package com.evolutiongenerator.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum AnimalBehaviourVariant {
    NORMAL, // The animal always performs the genes sequentially, one after the other.
    A_BIT_OF_MADNESS; // In 80% of the cases, the animal activates the gene immediately following the gene, but in 20% of the cases it jumps to another random gene.

    public String toString() {
        return switch (this) {
            case NORMAL -> "pełna predestynacja";
            case A_BIT_OF_MADNESS -> "nieco szaleństwa";
        };
    }

    public static AnimalBehaviourVariant fromString(String text) throws IllegalArgumentException{
        return Arrays.stream(AnimalBehaviourVariant.values())
                .filter(behaviourVariant -> behaviourVariant.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + AnimalBehaviourVariant.class.getCanonicalName() + "." + text));
    }

    public static List<String> getValuesAsStringList() {
        return Stream.of(AnimalBehaviourVariant.values()).map(Enum::toString).toList();
    }
}
