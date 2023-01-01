package com.evolutiongenerator.model.utils;
import com.evolutiongenerator.utils.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2dTest {
    @Test
    public void testEqualsWithTheSameVector() {
        Vector2d vector = new Vector2d(2, 1);
        assertTrue(vector.equals(vector));
    }

    @Test
    public void testEqualsWithDifferentVectorSameCoords() {
        Vector2d vector = new Vector2d(2, 1);
        Vector2d vector2 = new Vector2d(2, 1);
        assertTrue(vector.equals(vector2));
    }

    @Test
    public void testEqualsWithDifferentVectorDifferentCoords() {
        Vector2d vector = new Vector2d(2, 1);
        Vector2d vector2 = new Vector2d(3, 7);
        assertFalse(vector.equals(vector2));
    }

    // Method toString()
    @Test
    public void testToString() {
        Vector2d vector = new Vector2d(123, 4342);
        assertEquals("(123,4342)", vector.toString());
    }

    // Method precedes(Vector2d other)
    @Test
    public void testPrecedesTrue() {
        Vector2d vector = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(37, 21);
        assertTrue(vector.precedes(vector2));
    }

    @Test
    public void testPrecedesFalse() {
        Vector2d vector = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(-37, -21);
        assertFalse(vector.precedes(vector2));
    }

    // Method follows(Vector2d other)
    @Test
    public void testFollowsTrue() {
        Vector2d vector = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(-37, -21);
        assertTrue(vector.follows(vector2));
    }

    @Test
    public void testFollowsFalse() {
        Vector2d vector = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(37, 21);
        assertFalse(vector.follows(vector2));
    }

    // Method upperRight(Vector2d other)
    @Test
    public void testUpperRightFirstQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(15, 32);
        assertEquals(new Vector2d(15, 32), vector.upperRight(vector2));
    }

    @Test
    public void testUpperRightSecondQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(-11, 1);
        assertEquals(new Vector2d(0, 1), vector.upperRight(vector2));
    }

    @Test
    public void testUpperRightThirdQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(-15, -12);
        assertEquals(new Vector2d(0, 0), vector.upperRight(vector2));
    }

    @Test
    public void testUpperRightFourthQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(15, -123);
        assertEquals(new Vector2d(15, 0), vector.upperRight(vector2));
    }

    @Test
    public void testUpperRightNoRectangle() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(15, 0);
        assertEquals(null, vector.upperRight(vector2));
    }

    // Method lowerLeft(Vector2d other)
    @Test
    public void testLowerLeftFirstQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(15, 32);
        assertEquals(new Vector2d(0, 0), vector.lowerLeft(vector2));
    }

    @Test
    public void testLowerLeftSecondQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(-11, 1);
        assertEquals(new Vector2d(-11, 0), vector.lowerLeft(vector2));
    }

    @Test
    public void testLowerLeftThirdQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(-15, -12);
        assertEquals(new Vector2d(-15, -12), vector.lowerLeft(vector2));
    }

    @Test
    public void testLowerLeftFourthQuarter() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(15, -123);
        assertEquals(new Vector2d(0, -123), vector.lowerLeft(vector2));
    }

    @Test
    public void testLowerLeftNoRectangle() {
        Vector2d vector = new Vector2d(0, 0);
        Vector2d vector2 = new Vector2d(0, -123);
        assertEquals(null, vector.lowerLeft(vector2));
    }

    // Method add(Vector2d other)
    @Test
    public void testAdd() {
        Vector2d vector = new Vector2d(52, -12);
        Vector2d vector2 = new Vector2d(17, 80);
        assertEquals(new Vector2d(69, 68), vector.add(vector2));
    }

    // Method subtract(Vector2d other)
    @Test
    public void testSubstract() {
        Vector2d vector = new Vector2d(52, 11);
        Vector2d vector2 = new Vector2d(17, 58);
        assertEquals(new Vector2d(35, -47), vector.subtract(vector2));
    }

    // Method opposite()
    @Test
    public void testOpposite() {
        Vector2d vector = new Vector2d(12, -24);
        assertEquals(new Vector2d(-12, 24), vector.opposite());
    }

}
