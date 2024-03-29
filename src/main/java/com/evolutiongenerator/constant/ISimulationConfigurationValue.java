package com.evolutiongenerator.constant;

/**
 * Interface representing simulation variants.
 * <p>
 * Note: Every instance implementing this interface must have a static ISimulationConfigurationValue fromString(String value)
 * method which parses the given string and returns the corresponding ISimulationConfigurationValue instance. Unfortunately, it's
 * not possible to extend enums or even interfaces with static methods, so this is the only way to do it.
 *
 * @author Patryk Klatka
 */
public interface ISimulationConfigurationValue {
    String toString();
}
