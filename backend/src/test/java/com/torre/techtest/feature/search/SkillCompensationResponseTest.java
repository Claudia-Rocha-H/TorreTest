package com.torre.techtest.feature.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SkillCompensationResponseTest {

    @Test
    void shouldExposeAllProperties() {
        SkillCompensationResponse response = new SkillCompensationResponse("java");
        response.setAverageCompensation(1.0);
        response.setMedianCompensation(2.0);
        response.setMinCompensation(3.0);
        response.setMaxCompensation(4.0);
        response.setCurrency("COP");
        response.setPeriodicity("monthly");
        response.setDataPoints(5);
        response.setSource("custom");

        assertEquals("java", response.getSkill());
        assertEquals(1.0, response.getAverageCompensation());
        assertEquals(2.0, response.getMedianCompensation());
        assertEquals(3.0, response.getMinCompensation());
        assertEquals(4.0, response.getMaxCompensation());
        assertEquals("COP", response.getCurrency());
        assertEquals("monthly", response.getPeriodicity());
        assertEquals(5, response.getDataPoints());
        assertEquals("custom", response.getSource());
    }

    @Test
    void nullValues() {
        SkillCompensationResponse response = new SkillCompensationResponse("python");
        response.setSkill(null);
        response.setCurrency(null);

        assertEquals(null, response.getSkill());
        assertEquals(null, response.getCurrency());
    }

    @Test
    void zeroValues() {
        SkillCompensationResponse response = new SkillCompensationResponse("rust");
        response.setAverageCompensation(0.0);
        response.setDataPoints(0);

        assertEquals(0.0, response.getAverageCompensation());
        assertEquals(0, response.getDataPoints());
    }

    @Test
    void negativeValues() {
        SkillCompensationResponse response = new SkillCompensationResponse("go");
        response.setMinCompensation(-1000.0);

        assertEquals(-1000.0, response.getMinCompensation());
    }

    @Test
    void largeNumbers() {
        SkillCompensationResponse response = new SkillCompensationResponse("typescript");
        response.setMaxCompensation(999999999.99);
        response.setDataPoints(1000000);

        assertEquals(999999999.99, response.getMaxCompensation());
        assertEquals(1000000, response.getDataPoints());
    }

    @Test
    void decimalPrecision() {
        SkillCompensationResponse response = new SkillCompensationResponse("kotlin");
        response.setAverageCompensation(1234.56789);

        assertEquals(1234.56789, response.getAverageCompensation());
    }

    @Test
    void emptyStringValues() {
        SkillCompensationResponse response = new SkillCompensationResponse("");
        response.setCurrency("");

        assertEquals("", response.getSkill());
        assertEquals("", response.getCurrency());
    }
}
