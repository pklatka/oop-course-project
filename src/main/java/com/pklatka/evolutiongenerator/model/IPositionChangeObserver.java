package com.pklatka.evolutiongenerator.model;

public interface IPositionChangeObserver {
    boolean positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
