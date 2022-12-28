package com.evolutiongenerator.constant;

public class StringValue implements ISimulationConfigurationValue {
    public String value;

    public StringValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

