package com.evolutiongenerator.constant;

/**
 * Interface representing simulation variants.
 *
 * IMPORTANT: Every instance implementing this interface must have a static ISimulationValue fromString(String value)
 * method which parses the given string and returns the corresponding ISimulationValue instance. Unfortunately, it's
 * not possible to extend enums or even interfaces with static methods, so this is the only way to do it.
 *
 * @author Patryk Klatka
 */
public interface ISimulationValue {
    String toString();
}
