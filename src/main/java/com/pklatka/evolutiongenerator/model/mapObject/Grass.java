package com.pklatka.evolutiongenerator.model.mapObject;

import com.pklatka.evolutiongenerator.utils.Vector2d;

import java.util.Objects;

public class Grass implements IMapElement {

    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grass grass = (Grass) o;
        return Objects.equals(position, grass.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    public String getImageResource() {
        return "grass.png";
    }

    public String getObjectLabel() {
        return "Trawa";
    }
}
