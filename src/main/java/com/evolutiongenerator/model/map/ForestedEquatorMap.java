package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

public class ForestedEquatorMap extends AbstractWorldMap implements IWorldMap{

    public ForestedEquatorMap(int width, int height, int plantValue, MapVariant mapVariant) {
        this.height = height;
        this.width = width;
        this.bottomLeftVector = new Vector2d(-width,0);
        this.topRightVector = new Vector2d(width,height);
        this.availableGrassFields = width * height;
        this.plantValue = plantValue;
        this.mapVariant = mapVariant;
    }

    @Override
    public Vector2d[] getMapBounds() {
        return mapBoundaries.getMapBounds();
    }

    @Override
    public void growGrass() {

        if (availableGrassFields <= 0) return;

        Vector2d equatorTopRight = getEquatorTopRight();
        Vector2d equatorBottomLeft = getEquatorBottomLeft();
        int tmpX;
        int tmpY;
        if(Randomize.generateBooleanWithProbability(0.8)){
             tmpX = Randomize.generateInt(equatorTopRight.x,equatorBottomLeft.x);
             tmpY = Randomize.generateInt(equatorTopRight.y,equatorBottomLeft.y);

            while (isPlantAt(new Vector2d(tmpX,tmpY))) {
                tmpX = Randomize.generateInt(equatorTopRight.x,equatorBottomLeft.x);
                tmpY = Randomize.generateInt(equatorTopRight.y,equatorBottomLeft.y);
            }

        } else{
             tmpX = Randomize.generateInt(this.topRightVector.x,this.bottomLeftVector.x);
             tmpY = Randomize.generateInt(topRightVector.y,bottomLeftVector.y);

            while ( (tmpY > equatorTopRight.y || tmpY < equatorBottomLeft.y)  && isPlantAt(new Vector2d(tmpX,tmpY))) {
                tmpX = Randomize.generateInt(this.topRightVector.x,this.bottomLeftVector.x);
                tmpY = Randomize.generateInt(topRightVector.y,bottomLeftVector.y);
            }
        }

        Vector2d plantPosition = new Vector2d(tmpX, tmpY);
        plantHashMap.put(plantPosition,new Plant(plantPosition,this.plantValue));
        this.availableGrassFields--;

    }


    private int getEquatorCellAmount(){
        int totalCells = topRightVector.y;
        return  (int) Math.floor( totalCells * 0.2);
    }

    /**
     * Since this is the equator we assume that it extends from the left to the right end of the map. The height of the equator is 20% of the number of rows
     * @return bottom left position of the equator
     */
    private Vector2d getEquatorBottomLeft(){
        int equatorCellAmount = getEquatorCellAmount();
        return new Vector2d(bottomLeftVector.x, this.height / 2 - equatorCellAmount/2);
    }
    /**
     * Since this is the equator we assume that it extends from the left to the right end of the map. The height of the equator is 20% of the number of rows
     * @return top right position of the equator
     */
    private Vector2d getEquatorTopRight(){
        int equatorCellAmount = getEquatorCellAmount();
        return new Vector2d(topRightVector.x, this.height / 2 + equatorCellAmount/2);
    }
}