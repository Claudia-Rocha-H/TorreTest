package com.torre.techtest.feature.search;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response from Torre.ai people search API.
 * Encapsulates search results with pagination information for frontend consumption.
 * 
 * This response structure supports both the raw Torre.ai results and client-side pagination
 * to provide a smooth user experience with large result sets.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    /** List of person search results from Torre.ai */
    private List<PersonResult> results;
    
    /** Pagination metadata for frontend display and navigation */
    private PaginationInfo pagination;

    /**
     * Backward compatibility constructor.
     * Creates a response with default pagination info when only results are provided.
     * 
     * @param results List of person results from Torre.ai API
     */
    public SearchResponse(List<PersonResult> results) {
        this.results = results;
        this.pagination = new PaginationInfo(results.size(), 1, 20, results.size());
    }

    /**
     * Represents a single person result from Torre.ai search.
     * Maps Torre.ai response fields to our internal structure.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonResult {
        /** Torre.ai Global ID (ggId) - unique identifier */
        private String id;
        
        /** Person's full name */
        private String name;
        
        /** Professional headline or title */
        private String professionalHeadline;
        
        /** Profile image URL (imageUrl from Torre.ai) */
        private String picture;
        
        /** Torre.ai username for profile routing (usually same as id) */
        private String username;
    }
    
    /**
     * Pagination metadata for frontend list navigation.
     * Provides information needed to display pagination controls and result counts.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationInfo {
        /** Total number of pages available */
        private int total;
        
        /** Current page number (1-based) */
        private int currentPage;
        
        /** Number of items per page */
        private int pageSize;
        
        /** Total number of results found */
        private int totalResults;
    }
}