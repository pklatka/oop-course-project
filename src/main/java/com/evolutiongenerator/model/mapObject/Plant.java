package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.utils.Vector2d;

import java.util.Objects;

public class Plant implements IMapElement {
    private Vector2d position;
    private int energy;


    public Plant(Vector2d position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(position, plant.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    public int getEnergy() {
        return energy;
    }
}
