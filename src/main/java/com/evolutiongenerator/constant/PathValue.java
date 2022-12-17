package com.evolutiongenerator.constant;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * String object wrapper, that additionally checks if the given string is a valid path.
 *
 * @author Patryk Klatka
 */
public class PathValue implements ISimulationConfigurationValue {
    private String path;

    public PathValue(String path) throws InvalidPathException {
        try {
            setValue(path);
        } catch (InvalidPathException e) {
            throw new InvalidPathException(e.getMessage(), e.getReason());
        }
    }

    public void setValue(String path) throws InvalidPathException {
        try {
            Paths.get(path);
            this.path = path;
        } catch (InvalidPathException ex) {
            throw new InvalidPathException(path, "Podana ścieżka nie ma poprawnego formatu");
        }
    }

    public String getValue() {
        return path;
    }

    @Override
    public String toString() {
        return getValue();
    }

    /**
     * Returns a parsed from string PathValue value
     *
     * @return ISimulationConfigurationValue value
     */
    public static ISimulationConfigurationValue fromString(String value) {
        return new PathValue(value);
    }
}
