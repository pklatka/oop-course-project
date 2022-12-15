package com.evolutiongenerator.handler;

import com.evolutiongenerator.constant.ISimulationValue;

/**
 * Interface for handlers of configuration fields
 *
 * @author Patryk Klatka
 */
public interface IConfigurationField {
    /**
     * Write value to field
     *
     * @author Patryk Klatka
     * @param text ISimulationValue value to write
     */
    void writeProperty(ISimulationValue text);

    /**
     * Get value from field
     *
     * @author Patryk Klatka
     * @return ISimulationValue value from field
     */
    ISimulationValue readProperty() throws IllegalArgumentException;
}
