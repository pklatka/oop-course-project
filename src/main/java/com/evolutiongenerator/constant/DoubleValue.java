package com.evolutiongenerator.constant;

import java.text.DecimalFormat;

public class DoubleValue implements ISimulationConfigurationValue {
    Double value;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public DoubleValue(Double value){
        this.value = value;
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
