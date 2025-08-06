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
import com.torre.techtest.utils.HtmlUtils;

/**
 * Service for Torre.ai search API integration with HTML entity decoding
 */
@Service
public class SearchService {

    private static final String TORRE_SEARCH_API_URL = "https://torre.ai/api/entities/_searchStream";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Searches Torre.ai streaming API for people with HTML entity decoding
     */
    public SearchResponse searchPeople(SearchRequest request) throws Exception {
        List<PersonResult> personResults = new ArrayList<>();
        System.out.println("DEBUG: Sending search request to Torre API with query: " + request.getQuery());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(TORRE_SEARCH_API_URL);
            
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (compatible; TorreSearchBot/1.0)");

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
                                    System.out.println("DEBUG: Available fields in Torre.ai response: " + node.fieldNames());
                                    node.fieldNames().forEachRemaining(fieldName -> 
                                        System.out.println("DEBUG: Field '" + fieldName + "' = " + node.get(fieldName).asText())
                                    );
                                    
                                    if (node.has("ggId") && node.has("name")) {
                                        String ggId = node.get("ggId").asText();
                                        String username = node.has("username") ? node.get("username").asText() : ggId;
                                        
                                        String decodedName = HtmlUtils.safeDecodeHtmlEntities(node.get("name").asText());
                                        String decodedHeadline = node.has("professionalHeadline") ? 
                                            HtmlUtils.safeDecodeHtmlEntities(node.get("professionalHeadline").asText()) : null;
                                        
                                        PersonResult person = new PersonResult(
                                                ggId,
                                                decodedName,
                                                decodedHeadline,
                                                node.has("imageUrl") ? node.get("imageUrl").asText() : null,
                                                username
                                        );
                                        personResults.add(person);
                                        System.out.println("DEBUG: Added person: " + person.getName() + " (ID: " + person.getId() + ", Username: " + username + ")");
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