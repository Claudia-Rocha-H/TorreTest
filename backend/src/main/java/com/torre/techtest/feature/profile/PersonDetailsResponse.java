package com.torre.techtest.feature.profile;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Torre.ai person profile details response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDetailsResponse {
    
    @JsonProperty("person")
    private Person person;
    
    @JsonProperty("strengths")
    private List<Skill> strengths;
    
    @JsonProperty("experiences")
    private List<Experience> experiences;
    
    @JsonProperty("education")
    private List<Education> education;

    /**
     * Person's basic profile information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Person {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("professionalHeadline")
        private String professionalHeadline;
        
        @JsonProperty("picture")
        private String picture;
        
        @JsonProperty("summaryOfBio")
        private String summaryOfBio;
        
        @JsonProperty("publicId")
        private String publicId;
        
        @JsonProperty("location")
        private Location location;
    }

    /**
     * Professional skill or strength.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Skill {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("experience")
        private String experience;
        
        @JsonProperty("proficiency")
        private String proficiency;
        
        @JsonProperty("weight")
        private Double weight;
    }

    /**
     * Professional experience entry.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Experience {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("organizations")
        private List<Organization> organizations;
        
        @JsonProperty("fromMonth")
        private String fromMonth;
        
        @JsonProperty("fromYear")
        private String fromYear;
        
        @JsonProperty("toMonth")
        private String toMonth;
        
        @JsonProperty("toYear")
        private String toYear;
    }

    /**
     * Organization or company information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Organization {
        @JsonProperty("id")
        private Long id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("publicId")
        private String publicId;
        
        @JsonProperty("picture")
        private String picture;
        
        @JsonProperty("theme")
        private String theme;
        
        @JsonProperty("serviceType")
        private String serviceType;
        
        @JsonProperty("websiteUrl")
        private String websiteUrl;
        
        @JsonProperty("about")
        private String about;
    }

    /**
     * Educational background entry.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Education {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("organizations")
        private List<Organization> organizations;
        
        @JsonProperty("fromMonth")
        private String fromMonth;
        
        @JsonProperty("fromYear")
        private String fromYear;
        
        @JsonProperty("toMonth")
        private String toMonth;
        
        @JsonProperty("toYear")
        private String toYear;
    }

    /**
     * Location information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("shortName")
        private String shortName;
        
        @JsonProperty("country")
        private String country;
        
        @JsonProperty("countryCode")
        private String countryCode;
        
        @JsonProperty("latitude")
        private Double latitude;
        
        @JsonProperty("longitude")
        private Double longitude;
        
        @JsonProperty("timezone")
        private String timezone;
        
        @JsonProperty("placeId")
        private String placeId;
    }
}