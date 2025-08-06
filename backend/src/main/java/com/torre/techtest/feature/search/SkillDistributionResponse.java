package com.torre.techtest.feature.search;

import java.util.List;

public class SkillDistributionResponse {
    private String skill;
    private List<ProficiencyLevel> distribution;
    private int totalProfiles;
    private String source = "Torre.ai";

    public static class ProficiencyLevel {
        private String level;
        private int percentage;
        private int count;
        private String averageExperience;

        public ProficiencyLevel() {}

        public ProficiencyLevel(String level, int percentage, int count, String averageExperience) {
            this.level = level;
            this.percentage = percentage;
            this.count = count;
            this.averageExperience = averageExperience;
        }

        // Getters and setters
        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getAverageExperience() {
            return averageExperience;
        }

        public void setAverageExperience(String averageExperience) {
            this.averageExperience = averageExperience;
        }
    }

    // Getters and setters
    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public List<ProficiencyLevel> getDistribution() {
        return distribution;
    }

    public void setDistribution(List<ProficiencyLevel> distribution) {
        this.distribution = distribution;
    }

    public int getTotalProfiles() {
        return totalProfiles;
    }

    public void setTotalProfiles(int totalProfiles) {
        this.totalProfiles = totalProfiles;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
