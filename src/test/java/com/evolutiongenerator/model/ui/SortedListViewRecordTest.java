package com.evolutiongenerator.model.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortedListViewRecordTest {

    @Test
    public void testToString() {
        SortedListViewRecord record = new SortedListViewRecord(5, "test");
        assertEquals("(5) test", record.toString());

        SortedListViewRecord record2 = new SortedListViewRecord(12, "super");
        assertEquals("(12) super", record2.toString());
    }

    @Test
    public void testEquals(){
        SortedListViewRecord record = new SortedListViewRecord(5, "test");
        SortedListViewRecord record2 = new SortedListViewRecord(5, "test");
        SortedListViewRecord record3 = new SortedListViewRecord(12, "super");
        assertEquals(record, record2);
        assertNotEquals(record, record3);
    }
}
