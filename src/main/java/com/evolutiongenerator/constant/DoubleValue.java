package com.evolutiongenerator.constant;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Wrapper for double value, which implements ISimulationConfigurationValue interface.
 *
 * @author Patryk Klatka
 */
public class DoubleValue implements ISimulationConfigurationValue {
    Double value;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public DoubleValue(Double value){
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleValue that = (DoubleValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return df.format(value);
    }
}
