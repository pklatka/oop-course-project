package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.model.ui.MapVisualizer;
import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final HashMap<Vector2d, TreeSet<Animal>> animalOnFields = new HashMap<>();
    protected final HashMap<Animal, Vector2d> animalHashMap= new HashMap<>();
    protected final HashMap<Vector2d, Plant> plantHashMap = new HashMap<>();
    protected final HashMap<Vector2d, Animal> deadAnimalsHashMap = new HashMap<>();

    protected final MapBoundary mapBoundaries = new MapBoundary();

    protected MapVariant mapVariant;
    protected Vector2d topRightVector;
    protected Vector2d bottomLeftVector;
    protected int availableGrassFields;
    protected int plantValue;
    protected int width;
    protected int height;

    // Helper for updating animal position (overridden in GrassField)
    protected void updateAnimalPosition(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        TreeSet<Animal> oldAnimalTreeSet = animalOnFields.get(oldPosition);
        TreeSet<Animal> newAnimalTreeSet = animalOnFields.get(newPosition);
        oldAnimalTreeSet.remove(animal);
        animalHashMap.remove(animal);

        if (newAnimalTreeSet == null) {
            newAnimalTreeSet = new TreeSet<>(Comparator.comparing(Animal::getEnergy));
            animalOnFields.put(newPosition, newAnimalTreeSet);
        }

        newAnimalTreeSet.add(animal);
        animalHashMap.put(animal,newPosition);
        mapBoundaries.positionChanged(animal,oldPosition, newPosition);
    }

    @Override
    public boolean positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        if (isInsideMap(newPosition)) {
            updateAnimalPosition(animal, oldPosition, newPosition);
            return true;
        }
        return false;
    }
    @Override
    public void cleanDeadAnimals(){
            for(Vector2d vector2d: deadAnimalsHashMap.keySet()){
                Animal animal = deadAnimalsHashMap.get(vector2d);
                animalHashMap.remove(animal);
                animalOnFields.get(vector2d).remove(animal);
                mapBoundaries.removePosition(vector2d);
            }
            deadAnimalsHashMap.clear();
        }


    @Override
    public boolean isInsideMap(Vector2d position) {
        return  position.precedes(topRightVector)
                && position.follows(bottomLeftVector);
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException {
        if (isInsideMap(animal.getPosition())) {
            TreeSet<Animal> animalSet = new TreeSet<>(Comparator.comparing(Animal::getEnergy));
            animalOnFields.put(animal.getPosition(),animalSet);
            animalHashMap.put(animal,animal.getPosition());
            animalSet.add(animal);
            mapBoundaries.addPosition(animal.getPosition());
            animal.addObserver(this);
            return true;
        }

        throw new IllegalArgumentException("Animal can't be placed on this position " + animal.getPosition());
    }

    /**
     * Indicates whether the field is occupied
     * @param position Position to check.
     * @return information on whether there is an object at a given position
     */
    @Override
    public boolean isOccupied(Vector2d position) {
        TreeSet<Animal> treeSet = animalOnFields.get(position);
            if (treeSet != null){
                return !animalOnFields.get(position).isEmpty() || plantHashMap.containsKey(position);
            }
            return plantHashMap.containsKey(position);
    }

    @Override
    public Plant getPlantFrom(Vector2d position){
        return plantHashMap.get(position);
    }

    @Override
    public TreeSet<Animal> getAnimalsFrom(Vector2d position){
        return animalOnFields.get(position);
    }

    /**
     * @param position to see if there is a plant
     * @return information about whether there is a plant in a given position
     */
    public boolean isPlantAt(Vector2d position){
        return plantHashMap.get(position) != null;
    }

    /**
     * Resolves conflict of priority to do surgery between animals
     * @param position Position on which the conflict occurred
     * @return  The animal that has priority to eat the plant/reproduction
     */
    public Animal resolveConflicts(Vector2d position){
        TreeSet<Animal> animals = getAnimalsFrom(position);

        Iterator<Animal> it = animals.iterator();
        Animal animal1 = it.next();
        Animal animal2 = it.next();

        if (animal1.getEnergy() > animal2.getEnergy()){
            return animal1;
        } else if (animal2.getEnergy() > animal1.getEnergy()){
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
     * @param position from which the plant is to be removed
     */
    public void removeGrass(Vector2d position){
        this.plantHashMap.remove(position);
        this.availableGrassFields++;
    }

    @Override
    public Vector2d getRelativePositionToMapVariant(Vector2d newPosition) {
        if (this.mapVariant == MapVariant.GLOBE){
            int tmpX = newPosition.x < bottomLeftVector.x ? topRightVector.x : newPosition.x;
            tmpX = tmpX > topRightVector.x ? bottomLeftVector.x : tmpX;
            int tmpY = Math.min(newPosition.y, topRightVector.y);
            return  new Vector2d(tmpX,tmpY);
        }
        int tmpX = Randomize.generateInt(topRightVector.x,bottomLeftVector.x);
        int tmpY = Randomize.generateInt(topRightVector.y, bottomLeftVector.y);
        return new Vector2d(tmpX,tmpY);
    }

    public abstract Vector2d[] getMapBounds();


    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        Vector2d[] mapBounds = getMapBounds();
        return mapVisualizer.draw(mapBounds[0], mapBounds[1]);
    }
}
