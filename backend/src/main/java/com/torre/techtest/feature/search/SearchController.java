package com.torre.techtest.feature.search;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Torre.ai search operations.
 * 
 * This controller acts as a proxy between the Next.js frontend and Torre.ai's search API
 */
@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*") 
public class SearchController {

    private final SearchService searchService;

    /**
     * Constructor injection for SearchService dependency.
     * 
     * @param searchService Service layer handling Torre.ai API communication
     */
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Handles POST requests to search for people through Torre.ai API.
     * 
     * This endpoint accepts a search query and optional pagination parameters,
     * forwards the request to Torre.ai, and returns formatted results with pagination info.
     * 
     * @param requestPayload Map containing:
     *                      - query (String, required): The search term
     *                      - limit (Integer, optional): Max results (default: 100)
     * @return ResponseEntity with SearchResponse containing results and pagination info,
     *         or error message with appropriate HTTP status
     */
    @PostMapping("/people")
    public ResponseEntity<?> searchPeople(@RequestBody java.util.Map<String, Object> requestPayload) {
        String query = (String) requestPayload.get("query");
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Search query cannot be empty.");
        }

        Integer limit = requestPayload.get("limit") != null ? 
            Integer.valueOf(requestPayload.get("limit").toString()) : 100;

        try {
            SearchRequest torreRequest = new SearchRequest(query, limit);
            SearchResponse response = searchService.searchPeople(torreRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error during people search: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(java.util.Collections.singletonMap("message", "Error searching people: " + e.getMessage()));
        }
    }
}