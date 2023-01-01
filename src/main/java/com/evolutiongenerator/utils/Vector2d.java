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
        // Fist quadrant
        if(this.x < other.x && this.y < other.y){
            return other;
        }

        // Second quadrant
        if (this.x > other.x && this.y < other.y){
            return new Vector2d(this.x, other.y);
        }

        // Third quadrant
        if (this.x > other.x && this.y > other.y){
            return this;
        }

        // Fourth quadrant
        if (this.x < other.x && this.y > other.y){
            return new Vector2d(other.x, this.y);
        }


        // If point other has at least one coordinate equal to this point
        // then rectangle won't be created
        return null;
    }

    public Vector2d lowerLeft(Vector2d other){
        // Fist quadrant
        if(this.x < other.x && this.y < other.y){
            return this;
        }

        // Second quadrant
        if (this.x > other.x && this.y < other.y){
            return new Vector2d(other.x, this.y);
        }

        // Third quadrant
        if (this.x > other.x && this.y > other.y){
            return other;
        }

        // Fourth quadrant
        if (this.x < other.x && this.y > other.y){
            return new Vector2d(this.x, other.y);
        }

        // If point other has at least one coordinate equal to this point
        // then rectangle won't be created
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
