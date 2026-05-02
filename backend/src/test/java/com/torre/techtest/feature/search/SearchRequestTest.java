package com.torre.techtest.feature.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class SearchRequestTest {

    @Test
    void testDefaultConstructor() {
        SearchRequest request = new SearchRequest("java developer");
        assertEquals("java developer", request.getQuery());
        assertEquals("person", request.getIdentityType());
        assertEquals(100, request.getLimit());
        assertTrue(request.getMeta());
        assertNotNull(request.getExcluding());
        assertTrue(request.getExcluding().isEmpty());
    }

    @Test
    void testConstructorWithLimit() {
        SearchRequest request = new SearchRequest("python", 50);
        assertEquals("python", request.getQuery());
        assertEquals(50, request.getLimit());
    }

    @Test
    void testConstructorSetsDefaults() {
        SearchRequest request = new SearchRequest("test");
        assertNull(request.getTorreGgId());
        assertNotNull(request.getExcludedPeople());
        assertTrue(request.getExcludedPeople().isEmpty());
        assertFalse(request.getExcludeContacts());
    }

    @Test
    void testSetterMethods() {
        SearchRequest request = new SearchRequest("test");
        request.setTorreGgId("ABC123");
        request.setIdentityType("organization");
        request.setLimit(25);
        request.setMeta(false);
        
        assertEquals("ABC123", request.getTorreGgId());
        assertEquals("organization", request.getIdentityType());
        assertEquals(25, request.getLimit());
        assertFalse(request.getMeta());
    }

    @Test
    void testJsonIncludeExcludesNull() {
        SearchRequest request = new SearchRequest("test");
        request.setTorreGgId(null);
        
        assertNull(request.getTorreGgId());
    }
}
