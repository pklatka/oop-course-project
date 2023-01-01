package com.evolutiongenerator.model.mapObject;

import com.evolutiongenerator.utils.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void testNextNorth(){
        MapDirection direction = MapDirection.NORTH;
        assertEquals(MapDirection.NORTH_EAST, direction.next());
    }

    @Test
    public void testNextNorthEast(){
        MapDirection direction = MapDirection.NORTH_EAST;
        assertEquals(MapDirection.EAST, direction.next());
    }

    @Test
    public void testNextEast(){
        MapDirection direction = MapDirection.EAST;
        assertEquals(MapDirection.SOUTH_EAST, direction.next());
    }

    @Test
    public void testNextSouthEast(){
        MapDirection direction = MapDirection.SOUTH_EAST;
        assertEquals(MapDirection.SOUTH, direction.next());
    }

    @Test
    public void testNextSouth(){
        MapDirection direction = MapDirection.SOUTH;
        assertEquals(MapDirection.SOUTH_WEST, direction.next());
    }

    @Test
    public void testNextSouthWest(){
        MapDirection direction = MapDirection.SOUTH_WEST;
        assertEquals(MapDirection.WEST, direction.next());
    }

    @Test
    public void testNextWest(){
        MapDirection direction = MapDirection.WEST;
        assertEquals(MapDirection.NORTH_WEST, direction.next());
    }

    @Test
    public void testNextNorthWest(){
        MapDirection direction = MapDirection.NORTH_WEST;
        assertEquals(MapDirection.NORTH, direction.next());
    }

    @Test
    public void testPreviousNorth(){
        MapDirection direction = MapDirection.NORTH;
        assertEquals(MapDirection.NORTH_WEST, direction.previous());
    }

    @Test
    public void testPreviousNorthWest(){
        MapDirection direction = MapDirection.NORTH_WEST;
        assertEquals(MapDirection.WEST, direction.previous());
    }

    @Test
    public void testPreviousWest(){
        MapDirection direction = MapDirection.WEST;
        assertEquals(MapDirection.SOUTH_WEST, direction.previous());
    }

    @Test
    public void testPreviousSouthWest(){
        MapDirection direction = MapDirection.SOUTH_WEST;
        assertEquals(MapDirection.SOUTH, direction.previous());
    }

    @Test
    public void testPreviousSouth(){
        MapDirection direction = MapDirection.SOUTH;
        assertEquals(MapDirection.SOUTH_EAST, direction.previous());
    }

    @Test
    public void testPreviousSouthEast(){
        MapDirection direction = MapDirection.SOUTH_EAST;
        assertEquals(MapDirection.EAST, direction.previous());
    }

    @Test
    public void testPreviousEast(){
        MapDirection direction = MapDirection.EAST;
        assertEquals(MapDirection.NORTH_EAST, direction.previous());
    }

    @Test
    public void testPreviousNorthEast(){
        MapDirection direction = MapDirection.NORTH_EAST;
        assertEquals(MapDirection.NORTH, direction.previous());
    }

    @Test
    public void testToUnitVectorNorth(){
        MapDirection direction = MapDirection.NORTH;
        assertEquals(new Vector2d(0,1), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorNorthEast(){
        MapDirection direction = MapDirection.NORTH_EAST;
        assertEquals(new Vector2d(1,1), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorEast(){
        MapDirection direction = MapDirection.EAST;
        assertEquals(new Vector2d(1,0), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorSouthEast(){
        MapDirection direction = MapDirection.SOUTH_EAST;
        assertEquals(new Vector2d(1,-1), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorSouth(){
        MapDirection direction = MapDirection.SOUTH;
        assertEquals(new Vector2d(0,-1), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorSouthWest(){
        MapDirection direction = MapDirection.SOUTH_WEST;
        assertEquals(new Vector2d(-1,-1), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorWest(){
        MapDirection direction = MapDirection.WEST;
        assertEquals(new Vector2d(-1,0), direction.toUnitVector());
    }

    @Test
    public void testToUnitVectorNorthWest(){
        MapDirection direction = MapDirection.NORTH_WEST;
        assertEquals(new Vector2d(-1,1), direction.toUnitVector());
    }

    @Test
    public void testToStringNorth(){
        MapDirection direction = MapDirection.NORTH;
        assertEquals("N", direction.toString());
    }

    @Test
    public void testToStringNorthEast(){
        MapDirection direction = MapDirection.NORTH_EAST;
        assertEquals("NE", direction.toString());
    }

    @Test
    public void testToStringEast(){
        MapDirection direction = MapDirection.EAST;
        assertEquals("E", direction.toString());
    }

    @Test
    public void testToStringSouthEast(){
        MapDirection direction = MapDirection.SOUTH_EAST;
        assertEquals("SE", direction.toString());
    }

    @Test
    public void testToStringSouth(){
        MapDirection direction = MapDirection.SOUTH;
        assertEquals("S", direction.toString());
    }

    @Test
    public void testToStringSouthWest(){
        MapDirection direction = MapDirection.SOUTH_WEST;
        assertEquals("SW", direction.toString());
    }

    @Test
    public void testToStringWest(){
        MapDirection direction = MapDirection.WEST;
        assertEquals("W", direction.toString());
    }

    @Test
    public void testToStringNorthWest(){
        MapDirection direction = MapDirection.NORTH_WEST;
        assertEquals("NW", direction.toString());
    }

    @Test
    public void testGetOppositeDirectionNorth(){
        MapDirection direction = MapDirection.NORTH;
        assertEquals(MapDirection.SOUTH, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionNorthEast(){
        MapDirection direction = MapDirection.NORTH_EAST;
        assertEquals(MapDirection.SOUTH_WEST, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionEast(){
        MapDirection direction = MapDirection.EAST;
        assertEquals(MapDirection.WEST, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionSouthEast(){
        MapDirection direction = MapDirection.SOUTH_EAST;
        assertEquals(MapDirection.NORTH_WEST, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionSouth(){
        MapDirection direction = MapDirection.SOUTH;
        assertEquals(MapDirection.NORTH, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionSouthWest(){
        MapDirection direction = MapDirection.SOUTH_WEST;
        assertEquals(MapDirection.NORTH_EAST, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionWest(){
        MapDirection direction = MapDirection.WEST;
        assertEquals(MapDirection.EAST, direction.getOppositeDirection());
    }

    @Test
    public void testGetOppositeDirectionNorthWest(){
        MapDirection direction = MapDirection.NORTH_WEST;
        assertEquals(MapDirection.SOUTH_EAST, direction.getOppositeDirection());
    }

}
