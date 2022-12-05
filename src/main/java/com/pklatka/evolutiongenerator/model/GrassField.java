package com.pklatka.evolutiongenerator.model;

import java.util.concurrent.ThreadLocalRandom;

public class GrassField extends AbstractWorldMap {

    private final int noOfGrassFields;

    public GrassField(int noOfGrassFields) {
        this.noOfGrassFields = noOfGrassFields;
        this.topRightVector = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.bottomLeftVector = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (int i = 0; i < noOfGrassFields; i++) {
            addGrass();
        }
    }

    private void addGrass() {
        Vector2d grassPosition;
        do {
            grassPosition = new Vector2d(
                    ThreadLocalRandom.current().nextInt(0, (int) Math.sqrt(noOfGrassFields * 10) + 1),
                    ThreadLocalRandom.current().nextInt(0, (int) Math.sqrt(noOfGrassFields * 10) + 1)
            );
        } while (isOccupied(grassPosition));
        mapBoundaries.addPosition(grassPosition);
        grassHashMap.put(grassPosition, new Grass(grassPosition));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalHashMap.containsKey(position) || grassHashMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animalHashMap.containsKey(position)) {
            return animalHashMap.get(position);
        }

        if (grassHashMap.containsKey(position)) {
            return grassHashMap.get(position);
        }

        return null;
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException {
        // Check if there is grass on animal position
        boolean wasGrassOnPosition = false;
        if (grassHashMap.containsKey(animal.getPosition())) {
            grassHashMap.remove(animal.getPosition());
            mapBoundaries.removePosition(animal.getPosition());
            wasGrassOnPosition = true;
        }

        if (canMoveTo(animal.getPosition())) {
            animalHashMap.put(animal.getPosition(), animal);
            mapBoundaries.addPosition(animal.getPosition());
            animal.addObserver(this);

            if (wasGrassOnPosition) {
                addGrass();
            }
            return true;
        }

        throw new IllegalArgumentException("Can't move animal to position " + animal.getPosition());
    }

    @Override
    protected void updateAnimalPosition(Vector2d oldPosition, Vector2d newPosition) {
        super.updateAnimalPosition(oldPosition, newPosition);

        // Eat grass
        if (grassHashMap.containsKey(newPosition)) {
            grassHashMap.remove(newPosition);
            mapBoundaries.removePosition(newPosition);
            addGrass();
        }
    }

    public Vector2d[] getMapBounds() {
        return mapBoundaries.getMapBounds();
    }
}