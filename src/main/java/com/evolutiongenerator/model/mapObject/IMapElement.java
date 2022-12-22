package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.constant.MapObjectType;

/**
 * Interface for all map elements
 *
 * @author Patryk Klatka, Paweł Motyka
 */
public interface IMapElement {

    /**
     * Get type of object
     *
     * @return type of the element
     */
    MapObjectType getObjectType();
}
