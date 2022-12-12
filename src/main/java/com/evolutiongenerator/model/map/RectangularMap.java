package com.evolutiongenerator.model.map;

import com.evolutiongenerator.utils.Vector2d;

public class RectangularMap extends AbstractWorldMap {
    public RectangularMap(int width, int height) {
        this.topRightVector = new Vector2d(width, height);
        this.bottomLeftVector = new Vector2d(0, 0);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalHashMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return animalHashMap.get(position);
    }

    @Override
    public Vector2d getRelativePositionToMapVariant(Vector2d newPosition) {
        return null;
    }

    @Override
    public void cleanDeathAnimals() {

    }

    public Vector2d[] getMapBounds() {
        return new Vector2d[]{bottomLeftVector, topRightVector};
    }
}