package com.evolutiongenerator.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MutationVariantTest {
    @Test
    public void testToStringRandom() {
        assertEquals("pełna losowość", MutationVariant.RANDOM.toString());
    }

    @Test
    public void testToStringSlightCorrection(){
        assertEquals("lekka korekta", MutationVariant.SLIGHT_CORRECTION.toString());
    }

    @Test
    public void testFromStringRandom(){
        assertEquals(MutationVariant.RANDOM, MutationVariant.fromString("pełna losowość"));
    }

    @Test
    public void testFromStringSlightCorrection(){
        assertEquals(MutationVariant.SLIGHT_CORRECTION, MutationVariant.fromString("lekka korekta"));
    }
}
