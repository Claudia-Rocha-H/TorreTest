package com.torre.techtest.feature.search;

public class SkillCompensationResponse {
    private String skill;
    private double averageCompensation;
    private double medianCompensation;
    private double minCompensation;
    private double maxCompensation;
    private String currency = "USD";
    private String periodicity = "yearly";
    private int dataPoints;
    private String source = "Torre.ai";

    // Constructors
    public SkillCompensationResponse() {}

    public SkillCompensationResponse(String skill) {
        this.skill = skill;
    }

    // Getters and setters
    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public double getAverageCompensation() {
        return averageCompensation;
    }

    public void setAverageCompensation(double averageCompensation) {
        this.averageCompensation = averageCompensation;
    }

    public double getMedianCompensation() {
        return medianCompensation;
    }

    public void setMedianCompensation(double medianCompensation) {
        this.medianCompensation = medianCompensation;
    }

    public double getMinCompensation() {
        return minCompensation;
    }

    public void setMinCompensation(double minCompensation) {
        this.minCompensation = minCompensation;
    }

    public double getMaxCompensation() {
        return maxCompensation;
    }

    public void setMaxCompensation(double maxCompensation) {
        this.maxCompensation = maxCompensation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public int getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(int dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
