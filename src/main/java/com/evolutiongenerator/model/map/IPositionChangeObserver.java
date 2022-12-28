package com.evolutiongenerator.model.map;

import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.utils.Vector2d;

public interface IPositionChangeObserver {
    boolean positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition);
}
