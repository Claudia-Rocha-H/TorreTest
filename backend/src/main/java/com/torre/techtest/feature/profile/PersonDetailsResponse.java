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
 * 
 * Maps the response from Torre.ai's GET /genome/bios/{username} endpoint
 * to provide comprehensive person profile information including personal details,
 * professional summary, skills, experiences, and education.
 * 
 * This DTO is designed to handle Torre.ai's BioPersonDTOSchema structure
 * while exposing only the most relevant fields for our frontend requirements.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDetailsResponse {
    
    /** Basic person information from Torre.ai profile */
    @JsonProperty("person")
    private Person person;
    
    /** List of person's professional strengths and skills */
    @JsonProperty("strengths")
    private List<Skill> strengths;
    
    /** List of professional experiences */
    @JsonProperty("experiences")
    private List<Experience> experiences;
    
    /** List of educational background */
    @JsonProperty("education")
    private List<Education> education;

    /**
     * Person's basic profile information.
     * Contains core identity and professional headline data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Person {
        /** Torre.ai unique identifier (ggId) */
        @JsonProperty("id")
        private String id;
        
        /** Full name of the person */
        @JsonProperty("name")
        private String name;
        
        /** Professional headline/title */
        @JsonProperty("professionalHeadline")
        private String professionalHeadline;
        
        /** Profile picture URL */
        @JsonProperty("picture")
        private String picture;
        
        /** Brief professional bio summary */
        @JsonProperty("summaryOfBio")
        private String summaryOfBio;
        
        /** Public username/identifier for routing */
        @JsonProperty("publicId")
        private String publicId;
        
        /** Location information */
        @JsonProperty("location")
        private Location location;
    }

    /**
     * Represents a professional skill or strength.
     * Contains skill name and proficiency information from Torre.ai's assessment.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Skill {
        /** Unique skill identifier */
        @JsonProperty("id")
        private String id;
        
        /** Human-readable skill name */
        @JsonProperty("name")
        private String name;
        
        /** Experience level with this skill (e.g., "master", "proficient") */
        @JsonProperty("experience")
        private String experience;
        
        /** Proficiency assessment (e.g., "expert", "proficient", "novice") */
        @JsonProperty("proficiency")
        private String proficiency;
        
        /** Skill weight/importance score */
        @JsonProperty("weight")
        private Double weight;
    }

    /**
     * Professional experience entry.
     * Represents work history and roles from Torre.ai profile.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Experience {
        /** Experience entry identifier */
        @JsonProperty("id")
        private String id;
        
        /** Job title or role name */
        @JsonProperty("name")
        private String name;
        
        /** List of organizations/companies for this role */
        @JsonProperty("organizations")
        private List<Organization> organizations;
        
        /** Start and end dates for the role */
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
     * Organization/company information within experiences.
     */
    /**
     * Organization or company information.
     * Contains organization details from Torre.ai profiles.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Organization {
        /** Organization unique identifier */
        @JsonProperty("id")
        private Long id;
        
        /** Organization name */
        @JsonProperty("name")
        private String name;
        
        /** Organization public identifier */
        @JsonProperty("publicId")
        private String publicId;
        
        /** Organization logo/picture URL */
        @JsonProperty("picture")
        private String picture;
        
        /** Organization theme */
        @JsonProperty("theme")
        private String theme;
        
        /** Service type */
        @JsonProperty("serviceType")
        private String serviceType;
        
        /** Website URL (optional) */
        @JsonProperty("websiteUrl")
        private String websiteUrl;
        
        /** Organization description (optional) */
        @JsonProperty("about")
        private String about;
    }

    /**
     * Educational background entry.
     * Represents academic history from Torre.ai profile.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Education {
        /** Education entry identifier */
        @JsonProperty("id")
        private String id;
        
        /** Degree or program name */
        @JsonProperty("name")
        private String name;
        
        /** List of educational institutions */
        @JsonProperty("organizations")
        private List<Organization> organizations;
        
        /** Education timeline */
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
     * Represents location information.
     * Contains geographic and timezone data from Torre.ai profiles.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        /** Full location name */
        @JsonProperty("name")
        private String name;
        
        /** Short location name */
        @JsonProperty("shortName")
        private String shortName;
        
        /** Country name */
        @JsonProperty("country")
        private String country;
        
        /** ISO country code */
        @JsonProperty("countryCode")
        private String countryCode;
        
        /** Geographic latitude */
        @JsonProperty("latitude")
        private Double latitude;
        
        /** Geographic longitude */
        @JsonProperty("longitude")
        private Double longitude;
        
        /** Timezone identifier */
        @JsonProperty("timezone")
        private String timezone;
        
        /** Google Places ID */
        @JsonProperty("placeId")
        private String placeId;
    }
}
