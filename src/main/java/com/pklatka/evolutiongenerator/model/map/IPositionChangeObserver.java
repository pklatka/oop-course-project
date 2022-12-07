package com.pklatka.evolutiongenerator.model.map;

import com.pklatka.evolutiongenerator.utils.Vector2d;

public interface IPositionChangeObserver {
    boolean positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
