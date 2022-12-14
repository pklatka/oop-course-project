package com.evolutiongenerator.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum MapVariant  {
    GLOBE, // The left and right edges of the map loop (if the animal goes beyond the left edge, it will appear on the right side - and if it goes beyond the right edge, it will appear on the left); the top and bottom edges of the map are poles - you can't go there (if the animal tries to go beyond these edges of the map, it stays on the field it was on, and its direction changes to the opposite).
    INFERNAL_PORTAL; // If the pet goes beyond the edge of the map, it enters a magical portal; its energy is reduced by a certain value (the same as for the generation of a descendant), and it is then teleported to a new, random location on the map.

    public String toString() {
        return switch (this) {
            case GLOBE -> "kula ziemska";
            case INFERNAL_PORTAL -> "piekielny portal";
        };
    }

    public static Object fromString(String text) {
        return Arrays.stream(MapVariant.values())
                .filter(mapVariant -> mapVariant.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + MapVariant.class.getCanonicalName() + "." + text));
    }

    public static List<String> getValuesAsStringList() {
        return Stream.of(MapVariant.values()).map(Enum::toString).toList();
    }
}
