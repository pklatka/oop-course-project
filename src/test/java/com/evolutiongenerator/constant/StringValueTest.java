package com.evolutiongenerator.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringValueTest {
    @Test
    public void testStringValue(){
        StringValue stringValue = new StringValue("test");
        assertEquals("test", stringValue.toString());
    }

    @Test
    public void testStringValueInteger(){
        StringValue stringValue = new StringValue(1);
        assertEquals("1", stringValue.toString());
    }
}
