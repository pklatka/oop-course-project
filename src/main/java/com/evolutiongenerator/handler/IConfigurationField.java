package com.evolutiongenerator.handler;

import com.evolutiongenerator.constant.ISimulationConfigurationValue;

/**
 * Interface for configuration fields handlers.
 *
 * @author Patryk Klatka
 */
public interface IConfigurationField {
    /**
     * Write value to field.
     *
     * @param text ISimulationConfigurationValue value to write.
     */
    void writeProperty(ISimulationConfigurationValue text);

    /**
     * Get value from field.
     *
     * @return ISimulationConfigurationValue value from field.
     * @throws IllegalArgumentException if value is not in field.
     */
    ISimulationConfigurationValue readProperty() throws IllegalArgumentException;
}
