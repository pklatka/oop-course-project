package com.evolutiongenerator.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapVariantTest {

    @Test
    public void testToStringGlobe(){
        assertEquals("kula ziemska", MapVariant.GLOBE.toString());
    }

    @Test
    public void testToStringInfernalPortal(){
        assertEquals("piekielny portal", MapVariant.INFERNAL_PORTAL.toString());
    }

    @Test
    public void testFromStringGlobe(){
        assertEquals(MapVariant.GLOBE, MapVariant.fromString("kula ziemska"));
    }

    @Test
    public void testFromStringInfernalPortal(){
        assertEquals(MapVariant.INFERNAL_PORTAL, MapVariant.fromString("piekielny portal"));
    }

}
