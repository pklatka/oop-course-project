package com.evolutiongenerator.utils;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other){
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other){
        // Pierwsza ćwiartka
        if(this.x < other.x && this.y < other.y){
            return other;
        }

        // Druga ćwiartka
        if (this.x > other.x && this.y < other.y){
            return new Vector2d(this.x, other.y);
        }

        // Trzecia ćwiartka
        if (this.x > other.x && this.y > other.y){
            return this;
        }

        // Czwarta ćwiartka
        if (this.x < other.x && this.y > other.y){
            return new Vector2d(other.x, this.y);
        }

        // Jeżeli punkt other ma chociaż jedną współrzędną taką samą,
        // to nie powstanie prostokąt.
        return null;
    }

    public Vector2d lowerLeft(Vector2d other){
        // Pierwsza ćwiartka
        if(this.x < other.x && this.y < other.y){
            return this;
        }

        // Druga ćwiartka
        if (this.x > other.x && this.y < other.y){
            return new Vector2d(other.x, this.y);
        }

        // Trzecia ćwiartka
        if (this.x > other.x && this.y > other.y){
            return other;
        }

        // Czwarta ćwiartka
        if (this.x < other.x && this.y > other.y){
            return new Vector2d(this.x, other.y);
        }

        // Jeżeli punkt other ma chociaż jedną współrzędną taką samą,
        // to nie powstanie prostokąt.
        return null;
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector2d vector2d = (Vector2d) other;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }
}
