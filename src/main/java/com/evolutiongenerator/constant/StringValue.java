package com.evolutiongenerator.constant;

public class StringValue implements ISimulationConfigurationValue {
    public String value;

    public StringValue(String value) {
        this.value = value;
    }

    public StringValue(Integer value) {
        this.value = value.toString();
    }

    @Override
    public String toString() {
        return value;
    }
}

