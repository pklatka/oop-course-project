package com.evolutiongenerator.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantGrowthVariantTest {
    @Test
    public void testToStringForestedEquator(){
        assertEquals("zalesione równiki", PlantGrowthVariant.FORESTED_EQUATOR.toString());
    }

    @Test
    public void testToStringToxicCorpses(){
        assertEquals("toksyczne trupy", PlantGrowthVariant.TOXIC_CORPSES.toString());
    }

    @Test
    public void testFromStringForestedEquator(){
        assertEquals(PlantGrowthVariant.FORESTED_EQUATOR, PlantGrowthVariant.fromString("zalesione równiki"));
    }

    @Test
    public void testFromStringToxicCorpses(){
        assertEquals(PlantGrowthVariant.TOXIC_CORPSES, PlantGrowthVariant.fromString("toksyczne trupy"));
    }
}
