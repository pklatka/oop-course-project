package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.utils.Vector2d;

import java.util.Objects;

public class Plant implements IMapElement {
    private Vector2d position;
    private int energy;
    private boolean isOnEquator;


    public Plant(Vector2d position, int energy, boolean isOnEquator) {
        this.position = position;
        this.energy = energy;
        this.isOnEquator =isOnEquator;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return System.identityHashCode(o) == System.identityHashCode(this) && energy == plant.energy && isOnEquator == plant.isOnEquator && position.equals(plant.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, energy, isOnEquator) + System.identityHashCode(this);
    }

    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    public boolean isOnEquator() {
        return isOnEquator;
    }

    public int getEnergy() {
        return energy;
    }
}
