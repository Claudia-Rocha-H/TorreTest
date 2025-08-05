package com.torre.techtest.feature.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.torre.techtest.feature.search.SearchResponse.PersonResult;

/**
 * Service layer for Torre.ai search API integration.
 * 
 * This service acts as a proxy between our application and Torre.ai's _searchStream endpoint,
 * handling the complexities of:
 * - HTTP client configuration and connection management
 * - JSON streaming response parsing
 * - Error handling and logging
 * - Data transformation from Torre.ai format to our internal DTOs
 * 
 * The service processes Torre.ai's streaming JSON responses line by line to efficiently
 * handle large result sets while maintaining low memory usage.
 */
@Service
public class SearchService {

    /** Torre.ai search endpoint URL */
    private static final String TORRE_SEARCH_API_URL = "https://torre.ai/api/entities/_searchStream";
    
    /** Jackson ObjectMapper for JSON processing */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Searches for people through Torre.ai's streaming search API.
     * 
     * This method handles the complete flow of:
     * 1. Preparing and sending HTTP request to Torre.ai with proper headers
     * 2. Processing the streaming JSON response line by line
     * 3. Parsing each JSON object and extracting person data
     * 4. Transforming Torre.ai field names to our internal structure
     * 5. Building and returning a comprehensive SearchResponse
     * 
     * Torre.ai returns streaming JSON where each line is a separate JSON object
     * representing a person. We parse these incrementally to handle large result sets efficiently.
     * 
     * @param request SearchRequest containing query parameters and search configuration
     * @return SearchResponse with list of PersonResult objects and pagination info
     * @throws Exception if HTTP request fails, JSON parsing errors occur, or Torre.ai returns error status
     */
    public SearchResponse searchPeople(SearchRequest request) throws Exception {
        List<PersonResult> personResults = new ArrayList<>();
        System.out.println("DEBUG: Sending search request to Torre API with query: " + request.getQuery());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(TORRE_SEARCH_API_URL);
            
            // Headers según la documentación de Torre.ai
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (compatible; TorreSearchBot/1.0)");

            // Usar el objeto completo según SearchPeopleSchema
            String jsonPayload = objectMapper.writeValueAsString(request);
            httpPost.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));
            System.out.println("DEBUG: Request payload to Torre: " + jsonPayload);

            httpClient.execute(httpPost, response -> {
                System.out.println("DEBUG: Received response status from Torre: " + response.getCode() + " " + response.getReasonPhrase());

                if (response.getCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                        String line;
                        int lineNumber = 0;
                        while ((line = reader.readLine()) != null) {
                            lineNumber++;
                            if (!line.trim().isEmpty()) {
                                System.out.println("DEBUG: Processing line " + lineNumber + ": " + line);
                                try {
                                    JsonNode node = objectMapper.readTree(line);
                                    // Torre.ai devuelve campos diferentes: ggId, name, professionalHeadline, imageUrl
                                    if (node.has("ggId") && node.has("name")) {
                                        PersonResult person = new PersonResult(
                                                node.get("ggId").asText(),
                                                node.get("name").asText(),
                                                node.has("professionalHeadline") ? node.get("professionalHeadline").asText() : null,
                                                node.has("imageUrl") ? node.get("imageUrl").asText() : null
                                        );
                                        personResults.add(person);
                                        System.out.println("DEBUG: Added person: " + person.getName() + " (ID: " + person.getId() + ")");
                                    } else {
                                        System.out.println("DEBUG: Line " + lineNumber + " is not a valid person result (missing 'ggId' or 'name' fields): " + line);
                                    }
                                } catch (JsonProcessingException e) {
                                    System.err.println("ERROR: Error parsing JSON line " + lineNumber + ": " + line + " - " + e.getMessage());
                                }
                            } else {
                                System.out.println("DEBUG: Skipped empty line " + lineNumber);
                            }
                        }
                        System.out.println("DEBUG: Finished processing stream. Total persons found: " + personResults.size());
                    }
                } else {
                    String responseBody = "";
                    if (response.getEntity() != null) {
                        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                            responseBody = errorReader.lines().collect(Collectors.joining("\n"));
                        }
                    }
                    System.err.println("ERROR: Torre API returned non-200 status: " + response.getCode() + " - " + response.getReasonPhrase() + " - Body: " + responseBody);
                    throw new RuntimeException("Torre API returned error: " + response.getCode() + " - " + response.getReasonPhrase() + " - " + responseBody);
                }
                return null;
            });
        } catch (Exception e) {
            System.err.println("ERROR: Exception during HTTP client execution: " + e.getMessage());
            throw e;
        }

        return new SearchResponse(personResults);
    }
}