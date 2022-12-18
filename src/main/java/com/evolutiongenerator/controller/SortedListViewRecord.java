package com.evolutiongenerator.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SortedListViewRecord
{
    public final IntegerProperty priority = new SimpleIntegerProperty();
    private final StringProperty value = new SimpleStringProperty();


    public SortedListViewRecord(int priority, String value) {
        this.priority.set(priority);
        this.value.set(value);
    }

    @Override
    public String toString() {
        return value.get() + " (" + priority.get() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortedListViewRecord that = (SortedListViewRecord) o;
        return priority.get() == that.priority.get() &&
                value.get().equals(that.value.get());
    }
}
