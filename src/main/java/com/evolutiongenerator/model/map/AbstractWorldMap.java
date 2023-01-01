package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.model.ui.MapVisualizer;
import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract class representing a map of the world.
 *
 * @author Paweł Motyka
 */
public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final HashMap<Vector2d, TreeSet<Animal>> animalOnFields = new HashMap<>();
    protected final Map<Vector2d, Integer> mapDeathStat = new HashMap<>();
    protected final HashMap<Animal, Vector2d> animalHashMap = new HashMap<>();
    protected final HashMap<Vector2d, Plant> plantHashMap = new HashMap<>();
    protected final HashMap<Vector2d, Animal> deadAnimalsHashMap = new HashMap<>();
    protected final Set<Vector2d> conflictedPositions = new LinkedHashSet<>();
    protected Set<Vector2d> plantPositionsToConsume = new LinkedHashSet<>();


    protected final MapBoundary mapBoundaries = new MapBoundary();

    protected MapVariant mapVariant;
    protected Vector2d topRightVector;
    protected Vector2d bottomLeftVector;
    protected int availableGrassFields;
    protected int plantValue;
    protected int width;
    protected int height;

    /**
     * Used to update the position of the animal on the map and hashmaps
     *
     * @param animal      An animal that has changed position
     * @param oldPosition previous position of animal
     * @param newPosition new position of animal
     */
    protected void updateAnimalPosition(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        TreeSet<Animal> oldAnimalTreeSet = animalOnFields.get(oldPosition);
        TreeSet<Animal> newAnimalTreeSet = animalOnFields.get(newPosition);
        oldAnimalTreeSet.remove(animal);
        animalHashMap.remove(animal);

        if (newAnimalTreeSet == null) {
            newAnimalTreeSet = new TreeSet<>();
            animalOnFields.put(newPosition, newAnimalTreeSet);
        }

        newAnimalTreeSet.add(animal);
        animalHashMap.put(animal, newPosition);
        mapBoundaries.positionChanged(animal, oldPosition, newPosition);
    }

    @Override
    public boolean positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        if (isInsideMap(newPosition)) {
            updateAnimalPosition(animal, oldPosition, newPosition);
            return true;
        }
        return false;
    }

    @Override
    public List<Animal> cleanDeadAnimals() {
        List<Animal> animalsToRemove = new ArrayList<>();
        for (Vector2d vector2d : deadAnimalsHashMap.keySet()) {
            Animal animal = deadAnimalsHashMap.get(vector2d);
            int tmp = mapDeathStat.get(animal.getPosition()) != null ? mapDeathStat.get(animal.getPosition()) : 0;
            mapDeathStat.put(animal.getPosition(), tmp + 1);
            animalsToRemove.add(animal);
            animalHashMap.remove(animal);
            animalOnFields.get(vector2d).remove(animal);
            mapBoundaries.removePosition(vector2d);

            if (animalOnFields.get(vector2d).size() == 2) {
                conflictedPositions.remove(animal.getPosition());
            }

        }
        deadAnimalsHashMap.clear();

        return animalsToRemove;
    }


    @Override
    public boolean isInsideMap(Vector2d position) {
        return position.precedes(topRightVector)
                && position.follows(bottomLeftVector);
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException {
        if (isInsideMap(animal.getPosition())) {
            TreeSet<Animal> animalSet;
            if (animalOnFields.get(animal.getPosition()) == null) {
                animalSet = new TreeSet<>();
            } else {
                animalSet = animalOnFields.get(animal.getPosition());
            }
            animalOnFields.put(animal.getPosition(), animalSet);
            animalHashMap.put(animal, animal.getPosition());
            animalSet.add(animal);
            mapBoundaries.addPosition(animal.getPosition());
            animal.addObserver(this);
            return true;
        }

        throw new IllegalArgumentException("Animal can't be placed on this position " + animal.getPosition());
    }

    /**
     * Indicates whether the field is occupied
     *
     * @param position Position to check.
     * @return information on whether there is an object at a given position
     */
    @Override
    public boolean isOccupied(Vector2d position) {
        TreeSet<Animal> treeSet = animalOnFields.get(position);
        if (treeSet != null) {
            return !animalOnFields.get(position).isEmpty() || plantHashMap.containsKey(position);
        }
        return plantHashMap.containsKey(position);
    }

    @Override
    public Plant getPlantFrom(Vector2d position) {
        return plantHashMap.get(position);
    }

    @Override
    public void addPlantToConsume(Vector2d position) {
        plantPositionsToConsume.add(position);
    }

    @Override
    public Set<Vector2d> getPlantToConsume() {
        return plantPositionsToConsume;
    }

    @Override
    public TreeSet<Animal> getAnimalsFrom(Vector2d position) {
        return animalOnFields.get(position);
    }

    public boolean isPlantAt(Vector2d position) {
        return plantHashMap.get(position) != null;
    }

    public Animal resolveConflicts(Vector2d position, Animal animalToIgnore) {
        TreeSet<Animal> animals = getAnimalsFrom(position).stream().filter(a -> !a.equals(animalToIgnore) && a.getEnergy() > 0).collect(Collectors.toCollection(TreeSet::new));

        if (animals.size() == 0)
            return null;

        if (animals.size() == 1) {
            return animals.first();
        }

        Iterator<Animal> it = animals.descendingIterator();
        Animal animal1 = it.next();
        Animal animal2 = it.next();

        if (animal1.getEnergy() > animal2.getEnergy()) {
            return animal1;
        } else if (animal2.getEnergy() > animal1.getEnergy()) {
            return animal2;
        }

        if (animal1.getDays() > animal2.getDays())
            return animal1;

        if (animal2.getDays() > animal1.getDays())
            return animal2;

        if (animal1.getChildrenAmount() > animal2.getChildrenAmount())
            return animal1;

        if (animal2.getChildrenAmount() > animal1.getChildrenAmount())
            return animal2;

        return Randomize.generateBoolean() ? animal1 : animal2;
    }

    /**
     * Used to remove plants from the map
     *
     * @param position from which the plant is to be removed
     */
    public void removePlant(Vector2d position) {
        this.plantHashMap.remove(position);
        this.availableGrassFields++;
    }

    @Override
    public Vector2d getRelativePositionToMapVariant(Vector2d newPosition) {
        if (this.mapVariant == MapVariant.GLOBE) {
            int tmpX = newPosition.x < bottomLeftVector.x ? topRightVector.x : newPosition.x;
            tmpX = tmpX > topRightVector.x ? bottomLeftVector.x : tmpX;
            int tmpY = Math.min(newPosition.y, topRightVector.y);
            tmpY = Math.max(tmpY, bottomLeftVector.y);
            return new Vector2d(tmpX, tmpY);
        }
        int tmpX = Randomize.generateInt(topRightVector.x, bottomLeftVector.x);
        int tmpY = Randomize.generateInt(topRightVector.y, bottomLeftVector.y);
        return new Vector2d(tmpX, tmpY);
    }

    public boolean isAnimalChangingDirection(Vector2d newPosition) {
        if (this.mapVariant == MapVariant.GLOBE) {
            return newPosition.y > topRightVector.y || newPosition.y < bottomLeftVector.y;
        }
        return false;
    }

    @Override
    public Vector2d generateRandomPosition() {
        int tmpX = Randomize.generateInt(width, 0);
        int tmpY = Randomize.generateInt(height, 0);
        Vector2d position = new Vector2d(tmpX, tmpY);
        while (!isInsideMap(position)) {
            tmpX = Randomize.generateInt(width, 0);
            tmpY = Randomize.generateInt(height, 0);
            position = new Vector2d(tmpX, tmpY);
        }
        ;
        return position;
    }

    public void decreaseAnimalsEnergy() {
        for (Animal animal : animalHashMap.keySet()) {
            if (animal.getEnergy() > 0) {
                animal.decreaseEnergy(1);
                animal.increaseLivedDays();
            }

            if (animal.getEnergy() <= 0) {
                addDeadAnimal(animal);
            }

        }
    }

    public void addDeadAnimal(Animal animal) {
        deadAnimalsHashMap.put(animal.getPosition(), animal);
    }

    public abstract Vector2d[] getMapBounds();

    public void addReproduceConflictedPosition(Vector2d position) {
        this.conflictedPositions.add(position);
    }

    @Override
    public boolean isReproduceConflictsOccurred() {
        return !this.conflictedPositions.isEmpty();
    }

    @Override
    public MapVariant getMapVariant() {
        return mapVariant;
    }

    @Override
    public Set<Vector2d> getReproduceConflictedPositions() {
        return this.conflictedPositions;
    }

    public void cleanPlantsToConsume() {
        this.plantPositionsToConsume.clear();
    }

    public void clearReproduceConflictedPositions() {
        this.conflictedPositions.clear();
    }

    public void increasePlantSpaceAmount(){
        this.availableGrassFields++;
    }

    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        Vector2d[] mapBounds = getMapBounds();
        return mapVisualizer.draw(mapBounds[0], mapBounds[1]);
    }
}
