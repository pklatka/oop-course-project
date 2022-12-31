package com.evolutiongenerator.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class IntegerValueTest {

    @Test
    public void testEquals(){
        IntegerValue integerValue = new IntegerValue(1);
        IntegerValue integerValue1 = new IntegerValue(1);
        IntegerValue integerValue2 = new IntegerValue(2);

        assertEquals(integerValue, integerValue1);
        assertNotEquals(integerValue, integerValue2);
    }

    @Test
    public void testGetValue(){
        IntegerValue integerValue = new IntegerValue(1);
        assertEquals(1, integerValue.getValue());
    }

    @Test
    public void testSetValue(){
        IntegerValue integerValue = new IntegerValue(1);
        integerValue.setValue(2);
        assertEquals(2, integerValue.getValue());
    }

    @Test
    public void testToString(){
        IntegerValue integerValue = new IntegerValue(1);
        assertEquals("1", integerValue.toString());
    }

    @Test
    public void testToStringNegative(){
        IntegerValue integerValue = new IntegerValue(-1);
        assertEquals("-1", integerValue.toString());
    }

    @Test
    public void testFromString(){
        IntegerValue integerValue = new IntegerValue("1");
        assertEquals(1, integerValue.getValue());
    }
}
