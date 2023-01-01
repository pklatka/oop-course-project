package com.evolutiongenerator.model.map;

import com.evolutiongenerator.constant.MapVariant;
import com.evolutiongenerator.model.mapObject.Plant;
import com.evolutiongenerator.utils.Randomize;
import com.evolutiongenerator.utils.Vector2d;

public class ForestedEquatorMap extends AbstractWorldMap implements IWorldMap {
    private int plantsOnEquator;

    public ForestedEquatorMap(int width, int height, int plantValue, MapVariant mapVariant) {
        this.height = height;
        this.width = width;
        this.bottomLeftVector = new Vector2d(0, 0);
        this.topRightVector = new Vector2d(width - 1, height - 1);
        this.availableGrassFields = width * height;
        this.plantValue = plantValue;
        this.mapVariant = mapVariant;
        plantsOnEquator = 0;
    }

    @Override
    public Vector2d[] getMapBounds() {
        return mapBoundaries.getMapBounds();
    }

    @Override
    public Plant growPlant() {
        if (availableGrassFields <= 0 || plantHashMap.size() >= width * (height - 1)) return null;

        Vector2d equatorTopRight = getEquatorTopRight();
        Vector2d equatorBottomLeft = getEquatorBottomLeft();
        int tmpX;
        int tmpY;
        if (Randomize.generateBooleanWithProbability(0.8) && plantsOnEquator < getEquatorCellAmount()) {
            tmpX = Randomize.generateInt(equatorTopRight.x, equatorBottomLeft.x);
            tmpY = Randomize.generateInt(equatorTopRight.y, equatorBottomLeft.y);
            while (isPlantAt(new Vector2d(tmpX, tmpY))) {
                tmpX = Randomize.generateInt(equatorTopRight.x, equatorBottomLeft.x);
                tmpY = Randomize.generateInt(equatorTopRight.y, equatorBottomLeft.y);
            }
            Vector2d plantPosition = new Vector2d(tmpX, tmpY);
            Plant plantToGrow = new Plant(plantPosition, this.plantValue, true);
            plantHashMap.put(plantPosition, plantToGrow);
            plantsOnEquator++;
            return plantToGrow;
        } else {
            tmpX = Randomize.generateInt(this.topRightVector.x, this.bottomLeftVector.x);
            tmpY = Randomize.generateInt(topRightVector.y, bottomLeftVector.y);

            if (plantHashMap.size() - plantsOnEquator >= width * (height - 1)  )
                return null;

            while (!(tmpY > equatorTopRight.y || tmpY < equatorBottomLeft.y) || isPlantAt(new Vector2d(tmpX, tmpY))) {
                tmpX = Randomize.generateInt(this.topRightVector.x, this.bottomLeftVector.x);
                tmpY = Randomize.generateInt(topRightVector.y, bottomLeftVector.y);
            }
        }

        Vector2d plantPosition = new Vector2d(tmpX, tmpY);
        Plant plantToGrow = new Plant(plantPosition, this.plantValue, false);
        plantHashMap.put(plantPosition, plantToGrow);
        this.availableGrassFields--;
        return plantToGrow;
    }


    private int getEquatorHeightCellAmount() {
        int totalCells = topRightVector.y;
        return (int) Math.floor(totalCells * 0.2);
    }

    /**
     * Since this is the equator we assume that it extends from the left to the right end of the map. The height of the equator is 20% of the number of rows
     *
     * @return bottom left position of the equator
     */
    private Vector2d getEquatorBottomLeft() {
        int equatorCellAmount = getEquatorHeightCellAmount();
        return new Vector2d(bottomLeftVector.x, (this.height - 1) / 2 - equatorCellAmount / 2);
    }

    /**
     * Since this is the equator we assume that it extends from the left to the right end of the map. The height of the equator is 20% of the number of rows
     *
     * @return top right position of the equator
     */
    private Vector2d getEquatorTopRight() {
        int equatorCellAmount = getEquatorHeightCellAmount();
        return new Vector2d(topRightVector.x, (this.height - 1) / 2 + equatorCellAmount / 2);
    }

    private int getEquatorCellAmount(){
        return getEquatorHeightCellAmount() * width;
    }

    public void decreaseEquatorPlantAmount(){
        this.plantsOnEquator--;
    }
}