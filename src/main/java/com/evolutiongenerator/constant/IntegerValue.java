package com.evolutiongenerator.constant;

import java.util.Objects;

/**
 * Integer object wrapper, that implements ISimulationValue interface.
 *
 * @author Patryk Klatka
 */
public class IntegerValue implements ISimulationConfigurationValue {
    private Integer value;

    public IntegerValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerValue that = (IntegerValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public IntegerValue(String value) throws IllegalArgumentException {
        try {
            this.value = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wartość nie jest liczbą całkowitą");
        }
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Returns a parsed from string IntegerValue value
     *
     * @return ISimulationConfigurationValue value
     */
    public static ISimulationConfigurationValue fromString(String value) {
        return new IntegerValue(value);
    }
}
