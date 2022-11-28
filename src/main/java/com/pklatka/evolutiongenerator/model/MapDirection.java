package com.pklatka.evolutiongenerator.model;

public enum MapDirection {
    NORTH, WEST, SOUTH, EAST;

    public String toString(){
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
        };
    }

    public MapDirection next(){
        return MapDirection.values()[this.ordinal() == 0 ? 3 : this.ordinal() - 1];
    }

    public MapDirection previous(){
        return MapDirection.values()[(this.ordinal() + 1) % 4];
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2d(0,1);
            case SOUTH -> new Vector2d(0,-1);
            case EAST -> new Vector2d(1,0);
            case WEST -> new Vector2d(-1,0);
        };
    }
}
