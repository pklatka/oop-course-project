package com.evolutiongenerator.model.utils;

import com.evolutiongenerator.utils.PathValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathValueTest {

    @Test
    public void testGetPath() {
        assertThrows(IllegalArgumentException.class, () -> new PathValue("t//\\\" \0/est"));
        assertDoesNotThrow(() -> new PathValue("C:/users/local"));
    }

    @Test
    public void testGetPathValue() {
        PathValue path = new PathValue("C:/users/local");
        assertEquals("C:/users/local", path.getValue());
    }

    @Test
    public void testToString() {
        PathValue path = new PathValue("C:/users/local");
        assertEquals("C:/users/local", path.toString());
    }

    @Test
    public void testEquals(){
        PathValue path = new PathValue("C:/users/local");
        PathValue path2 = new PathValue("C:/users/local");
        PathValue path3 = new PathValue("C:/users/local2");
        assertEquals(path, path2);
        assertNotEquals(path, path3);
    }
}
