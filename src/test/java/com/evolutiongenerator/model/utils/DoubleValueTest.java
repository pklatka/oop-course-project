package com.evolutiongenerator.model.utils;

import com.evolutiongenerator.utils.DoubleValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DoubleValueTest {

    @Test
    public void testEquals(){
        DoubleValue doubleValue = new DoubleValue(1.0);
        DoubleValue doubleValue1 = new DoubleValue(1.0);
        DoubleValue doubleValue2 = new DoubleValue(2.0);

        assertEquals(doubleValue, doubleValue1);
        assertNotEquals(doubleValue, doubleValue2);
    }

    @Test
    public void testGetValue(){
        DoubleValue doubleValue = new DoubleValue(1.0);
        assertEquals(1.0, doubleValue.getValue());
    }

    @Test
    public void testSetValue(){
        DoubleValue doubleValue = new DoubleValue(1.0);
        doubleValue.setValue(2.0);
        assertEquals(2.0, doubleValue.getValue());
    }

    @Test
    public void testToString(){
        DoubleValue doubleValue = new DoubleValue(1.0);
        assertEquals("1.00", doubleValue.toString());
    }

    @Test
    public void testToStringNegative(){
        DoubleValue doubleValue = new DoubleValue(-1.01);
        assertEquals("-1.01", doubleValue.toString());
    }
}
