package com.evolutiongenerator.model.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Wrapper class for storing data in ListView
 *
 * @author Patryk Klatka
 */
public class SortedListViewRecord {
    public final IntegerProperty priority = new SimpleIntegerProperty();
    public final StringProperty value = new SimpleStringProperty();


    /**
     * SortedListViewRecord constructor.
     *
     * @param priority Priority (the smaller the value, the higher the priority)
     * @param value    Value
     */
    public SortedListViewRecord(int priority, String value) {
        this.priority.set(priority);
        this.value.set(value);
    }

    /**
     * Gets record as string.
     *
     * @return String representation of record
     */
    @Override
    public String toString() {
        return value.get() + " (" + priority.get() + ")";
    }

    /**
     * Checks if two records are equal.
     *
     * @return True if records are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortedListViewRecord that = (SortedListViewRecord) o;
        return priority.get() == that.priority.get() &&
                value.get().equals(that.value.get());
    }
}
