package com.evolutiongenerator.utils;

import com.evolutiongenerator.constant.ISimulationConfigurationValue;

/**
 * String object wrapper which implements ISimulationConfigurationValue.
 *
 * @author Patryk Klatka
 */
public class StringValue implements ISimulationConfigurationValue {
    public String value;

    public StringValue(String value) {
        this.value = value;
    }

    public StringValue(Integer value) {
        this.value = value.toString();
    }

    public StringValue(int value) {
        this.value = Integer.toString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

