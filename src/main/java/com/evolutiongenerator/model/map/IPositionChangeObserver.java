package com.evolutiongenerator.model.map;

import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.utils.Vector2d;

/**
 * Interface for classes that observe position changes of animals.
 *
 * @author Paweł Motyka
 */
public interface IPositionChangeObserver {
    /**
     * Called when animal changes position.
     *
     * @param animal      Animal that has changed position.
     * @param oldPosition Previous position of animal.
     * @param newPosition New position of animal.
     */
    boolean positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}
