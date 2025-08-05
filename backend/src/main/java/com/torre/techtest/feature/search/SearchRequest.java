package com.torre.techtest.feature.search;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for search requests to Torre.ai API following SearchPeopleSchema specification.
 * This class encapsulates all parameters required for searching people through Torre's _searchStream endpoint.
 * 
 * @see <a href="https://torre.ai/api/entities/_searchStream">Torre.ai Search API</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest {
    /** Search query string - the main search term */
    private String query;
    
    /** Torre.ai Global ID for specific entity searches */
    private String torreGgId;
    
    /** Type of identity to search for (e.g., "person", "organization") */
    private String identityType;
    
    /** Maximum number of results to return (1-100) */
    private Integer limit;
    
    /** Whether to include metadata in the response */
    private Boolean meta;
    
    /** List of entity types to exclude from results */
    private List<String> excluding;
    
    /** List of specific people IDs to exclude from results */
    private List<String> excludedPeople;
    
    /** Whether to exclude contacts from results */
    private Boolean excludeContacts;
    
    /**
     * Constructor for simple search with optimized defaults.
     * Sets up a search request with sensible defaults for people search with higher result limit.
     * 
     * @param query The search term to look for
     */
    public SearchRequest(String query) {
        this.query = query;
        this.torreGgId = null;
        this.identityType = "person";
        this.limit = 100; // Increased limit to get more comprehensive results
        this.meta = true;
        this.excluding = new ArrayList<>();
        this.excludedPeople = new ArrayList<>();
        this.excludeContacts = false;
    }
    
    /**
     * Constructor for search with custom result pagination.
     * Allows fine-tuning of the number of results returned from Torre.ai API.
     * 
     * @param query The search term to look for
     * @param limit Custom limit for number of results (1-100)
     */
    public SearchRequest(String query, int limit) {
        this.query = query;
        this.torreGgId = null;
        this.identityType = "person";
        this.limit = limit;
        this.meta = true;
        this.excluding = new ArrayList<>();
        this.excludedPeople = new ArrayList<>();
        this.excludeContacts = false;
    }
}
