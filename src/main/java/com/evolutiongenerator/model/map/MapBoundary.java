package com.evolutiongenerator.model.map;

import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.utils.Vector2d;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Class representing the boundaries of the map.
 *
 * @author Patryk Klatka, Paweł Motyka
 */
public class MapBoundary implements IPositionChangeObserver {

    private final TreeMap<Vector2d, Integer> oxAxis = new TreeMap<>(Comparator.comparingInt(v -> v.x));
    private final TreeMap<Vector2d, Integer> oyAxis = new TreeMap<>(Comparator.comparingInt(v -> v.y));

    public void removePosition(Vector2d position) {
        if (oxAxis.containsKey(position)) {
            if (oxAxis.get(position) == 1) {
                oxAxis.remove(position);
            } else {
                oxAxis.put(position, oxAxis.get(position) - 1);
            }
        }

        if (oyAxis.containsKey(position)) {
            if (oyAxis.get(position) == 1) {
                oyAxis.remove(position);
            } else {
                oyAxis.put(position, oyAxis.get(position) - 1);
            }
        }

    }

    public void addPosition(Vector2d position) {
        if (oxAxis.containsKey(position)) {
            oxAxis.put(position, oxAxis.get(position) + 1);
        } else {
            oxAxis.put(position, 1);
        }

        if (oyAxis.containsKey(position)) {
            oyAxis.put(position, oyAxis.get(position) + 1);
        } else {
            oyAxis.put(position, 1);
        }
    }

    @Override
    public boolean positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        removePosition(oldPosition);
        addPosition(newPosition);
        return true;
    }


    public Vector2d[] getMapBounds() {
        return new Vector2d[]{new Vector2d(oxAxis.firstKey().x, oyAxis.firstKey().y), new Vector2d(oxAxis.lastKey().x, oyAxis.lastKey().y)};
    }

}
