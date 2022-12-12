package com.evolutiongenerator.model.map;

import com.evolutiongenerator.utils.Vector2d;

public interface IPositionChangeObserver {
    boolean positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
