package com.evolutiongenerator.model.mapObject.Animal;

import com.evolutiongenerator.model.map.IPositionChangeObserver;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.mapObject.IMapElement;
import com.evolutiongenerator.model.mapObject.MapDirection;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Animal implements IMapElement {
    private MapDirection heading = MapDirection.getRandomDirection();
    private Vector2d position;
    private int days ;
    private int childrenAmount;
    private IWorldMap map;
    private int energy;
    private Genes genes;
    private final int ENERGY_TO_REPRODUCE;
    private final int REPRODUCE_COST;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();


    public Animal(IWorldMap map, Vector2d initialPosition, Genes genes, int energy, int reproduceCost, int minimalValueToReproduce) {
        this.map = map;
        this.position = initialPosition;
        this.genes = genes;
        this.energy = energy;
        days = 0;
        childrenAmount = 0;
        ENERGY_TO_REPRODUCE = minimalValueToReproduce;
        REPRODUCE_COST = reproduceCost;
    }

    @Override
    public String toString() {
        return switch (heading){
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST-> "W";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return energy == animal.energy && ENERGY_TO_REPRODUCE == animal.ENERGY_TO_REPRODUCE && REPRODUCE_COST == animal.REPRODUCE_COST && heading == animal.heading && Objects.equals(map, animal.map) && Objects.equals(genes, animal.genes);
    }

    public int getDays() {
        return days;
    }

    public int getChildrenAmount() {
        return childrenAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, map, energy, genes, ENERGY_TO_REPRODUCE, REPRODUCE_COST);
    }

    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    // TODO clear?
    public MapDirection getHeading() {
        return switch (heading) {
            case NORTH -> MapDirection.NORTH;
            case WEST -> MapDirection.WEST;
            case SOUTH -> MapDirection.SOUTH;
            case EAST -> MapDirection.EAST;
            case NORTH_EAST -> MapDirection.NORTH_EAST;
            case NORTH_WEST -> MapDirection.NORTH_WEST;
            case SOUTH_EAST -> MapDirection.SOUTH_EAST;
            case SOUTH_WEST -> MapDirection.SOUTH_WEST;
        };
    }

    public void changeDirection(int gen){
        for (int i = 0; i < gen; i++)
            heading = heading.next();
    }

    public boolean isAt(Vector2d position) {
        return this.position.x == position.x && this.position.y == position.y;
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public int getEnergy() {
        return energy;
    }

    public Genes getGenome() {
        return genes;
    }

    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(this,oldPosition, newPosition);
        }
    }
    public void move() {
        int currentGen = genes.getGen();
        changeDirection(currentGen);
        Vector2d oldPosition = new Vector2d(position.x, position.y);
        Vector2d unitVector = heading.toUnitVector();
        position = position.add(unitVector);
        positionChanged(oldPosition,position);

        // TODO after implementing map variances, write their handlers
    }

    /**
     * We assume that in order for an animal to reproduce it needs the share of energy required for parenting to be less or equal than half of its current enrgy
     * @param parnter Reproduction partner
     * @return Descendant of parents (new Animal) if parents doesn't have enough energy returns null
     */
    public Animal reproduce(Animal parnter) throws IllegalStateException{

        if (!position.equals(parnter.position)) {
            throw new IllegalStateException("Animals are not at the same field!");
        }

        if (energy >= ENERGY_TO_REPRODUCE && parnter.energy >= ENERGY_TO_REPRODUCE ) {
            int descendantEnergy = REPRODUCE_COST * 2;
            int thisGenesForOffspringAmount = getGenesAmount(parnter);
            int partnerGenesForOffspringAmount = genes.getGenesSize() - thisGenesForOffspringAmount;
            boolean isLeftSideGenes = Randomize.generateBoolean();

            List<Integer> offspringGenes;
            if (energy > parnter.energy){
                offspringGenes = genes.getOffspringGenes(thisGenesForOffspringAmount,isLeftSideGenes);
                offspringGenes.addAll(parnter.genes.getOffspringGenes(partnerGenesForOffspringAmount,!isLeftSideGenes));
            }else {
                offspringGenes = parnter.genes.getOffspringGenes(partnerGenesForOffspringAmount,isLeftSideGenes);
                offspringGenes.addAll(genes.getOffspringGenes(thisGenesForOffspringAmount,!isLeftSideGenes));
            }
            energy -= REPRODUCE_COST;
            parnter.energy -= REPRODUCE_COST;
            childrenAmount += 1;
            parnter.childrenAmount += 1;
            return new Animal(map,new Vector2d(position.x,position.y),genes.createOffspringGenes(offspringGenes),descendantEnergy,REPRODUCE_COST, ENERGY_TO_REPRODUCE);
        }
        return null;
    }

    private int getGenesAmount(Animal partner){
        return Math.round((float) energy * genes.getGenesSize()/(energy + partner.energy));
    }
    public void consume(Plant plant){
        this.energy += plant.getEnergy();
    }

}