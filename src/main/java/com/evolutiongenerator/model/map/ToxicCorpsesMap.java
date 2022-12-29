package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

import java.util.*;

public class ToxicCorpsesMap extends AbstractWorldMap implements IWorldMap {

    public ToxicCorpsesMap(int width, int height, int plantValue, MapVariant mapVariant) {
        this.height = height;
        this.width = width;
        this.bottomLeftVector = new Vector2d(Integer.MIN_VALUE, 0);
        this.topRightVector = new Vector2d(Integer.MAX_VALUE, height);
        this.availableGrassFields = width * height;
        this.plantValue = plantValue;
        this.mapVariant = mapVariant;
    }

    @Override
    public Vector2d[] getMapBounds() {
        return this.mapBoundaries.getMapBounds();
    }

    @Override
    public void growPlant() {
        LinkedHashMap<Vector2d, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        if (availableGrassFields <= 0) return;

        for (Map.Entry<Vector2d, Integer> entry : mapDeathStat.entrySet()) {
            list.add(entry.getValue());
        }

        list.sort(Integer::compareTo);

        for (Integer str : list) {
            for (Map.Entry<Vector2d, Integer> entry : mapDeathStat.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedMap.put(entry.getKey(), str);
                }
            }
        }

        if (Randomize.generateBooleanWithProbability(0.8)) {
            for (Vector2d vector2d : sortedMap.keySet()) {
                if (!isPlantAt(vector2d)) {
                    this.plantHashMap.put(vector2d, new Plant(vector2d, plantValue));
                    this.availableGrassFields--;
                    return;
                }
            }
        } else {
            int tmpX = Randomize.generateInt(this.topRightVector.x, this.bottomLeftVector.x);
            int tmpY = Randomize.generateInt(topRightVector.y, bottomLeftVector.y);
            while (isPlantAt(new Vector2d(tmpX, tmpY))) {
                tmpX = Randomize.generateInt(this.topRightVector.x, this.bottomLeftVector.x);
                tmpY = Randomize.generateInt(topRightVector.y, bottomLeftVector.y);
            }
            this.availableGrassFields--;
        }


    }
}
