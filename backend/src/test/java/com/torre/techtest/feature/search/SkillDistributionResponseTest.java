package com.torre.techtest.feature.search;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SkillDistributionResponseTest {

    @Test
    void shouldExposeAllProperties() {
        SkillDistributionResponse.ProficiencyLevel level = new SkillDistributionResponse.ProficiencyLevel("expert", 100, 5, null);

        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("java");
        response.setDistribution(List.of(level));
        response.setTotalProfiles(5);
        response.setSource("custom");

        assertEquals("java", response.getSkill());
        assertEquals(1, response.getDistribution().size());
        assertEquals("expert", response.getDistribution().get(0).getLevel());
        assertEquals(100, response.getDistribution().get(0).getPercentage());
        assertEquals(5, response.getDistribution().get(0).getCount());
        assertEquals(5, response.getTotalProfiles());
        assertEquals("custom", response.getSource());
    }

    @Test
    void nullValues() {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill(null);
        response.setDistribution(null);
        response.setSource(null);

        assertEquals(null, response.getSkill());
        assertEquals(null, response.getDistribution());
        assertEquals(null, response.getSource());
    }

    @Test
    void zeroValues() {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setTotalProfiles(0);

        assertEquals(0, response.getTotalProfiles());
    }

    @Test
    void emptyDistribution() {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("python");
        response.setDistribution(List.of());

        assertEquals("python", response.getSkill());
        assertEquals(0, response.getDistribution().size());
    }

    @Test
    void multipleLevels() {
        SkillDistributionResponse.ProficiencyLevel beginner = new SkillDistributionResponse.ProficiencyLevel("beginner", 25, 10, null);
        SkillDistributionResponse.ProficiencyLevel intermediate = new SkillDistributionResponse.ProficiencyLevel("intermediate", 50, 20, null);
        SkillDistributionResponse.ProficiencyLevel expert = new SkillDistributionResponse.ProficiencyLevel("expert", 25, 10, null);

        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("typescript");
        response.setDistribution(List.of(beginner, intermediate, expert));
        response.setTotalProfiles(40);

        assertEquals(3, response.getDistribution().size());
        assertEquals(40, response.getTotalProfiles());
    }

    @Test
    void largeNumbers() {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setTotalProfiles(1000000);

        assertEquals(1000000, response.getTotalProfiles());
    }

    @Test
    void proficiencyLevelProperties() {
        SkillDistributionResponse.ProficiencyLevel level = new SkillDistributionResponse.ProficiencyLevel("advanced", 50, 100, "5.5");

        assertEquals("advanced", level.getLevel());
        assertEquals(50, level.getPercentage());
        assertEquals(100, level.getCount());
        assertEquals("5.5", level.getAverageExperience());
    }

    @Test
    void emptyStringSkill() {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("");

        assertEquals("", response.getSkill());
    }
}
