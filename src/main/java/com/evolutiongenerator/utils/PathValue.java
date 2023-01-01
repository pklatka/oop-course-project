package com.evolutiongenerator.utils;

import com.evolutiongenerator.constant.ISimulationConfigurationValue;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * String object wrapper, that additionally checks if the given string is a valid path.
 *
 * @author Patryk Klatka
 */
public class PathValue implements ISimulationConfigurationValue {
    private String path;

    /**
     * Constructor which creates PathValue from string.
     *
     * @param path String path.
     * @throws InvalidPathException if the string is not a valid path.
     */
    public PathValue(String path) throws InvalidPathException {
        try {
            setValue(path);
        } catch (InvalidPathException e) {
            throw new InvalidPathException(e.getMessage(), e.getReason());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathValue pathValue = (PathValue) o;
        return Objects.equals(path, pathValue.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    /**
     * Sets path from string.
     *
     * @param path String path.
     * @throws InvalidPathException if the string is not a valid path.
     */
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
     * Returns a parsed from string PathValue value.
     *
     * @return ISimulationConfigurationValue value.
     */
    public static ISimulationConfigurationValue fromString(String value) {
        return new PathValue(value);
    }
}
