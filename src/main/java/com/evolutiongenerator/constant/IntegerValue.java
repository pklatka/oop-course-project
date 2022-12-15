package com.evolutiongenerator.constant;

/**
 * Class, that wraps integer value
 *
 * @author Patryk Klatka
 */
public class IntegerValue implements ISimulationValue {
    private Integer value;

    public IntegerValue(Integer value) {
        this.value = value;
    }

    public IntegerValue(String value) throws IllegalArgumentException {
        try{
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
     * @author Patryk Klatka
     * @return ISimulationValue value
     */
    public static ISimulationValue fromString(String value) {
        return new IntegerValue(value);
    }
}
