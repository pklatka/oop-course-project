package com.evolutiongenerator.handler;

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
     * @param text String value to write
     */
    void writeProperty(String text);

    /**
     * Get value from field
     *
     * @author Patryk Klatka
     * @return Object value from field
     */
    Object readProperty();
}
