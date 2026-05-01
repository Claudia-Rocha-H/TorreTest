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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.torre.techtest.exception.ExternalServiceException;
import com.torre.techtest.feature.search.SearchResponse.PersonResult;
import com.torre.techtest.utils.HtmlUtils;

/**
 * Service for Torre.ai search API integration with HTML entity decoding
 */
@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private static final String TORRE_SEARCH_API_URL = "https://torre.ai/api/entities/_searchStream";
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String getSearchApiUrl() {
        return TORRE_SEARCH_API_URL;
    }

    /**
     * Searches Torre.ai streaming API for people with HTML entity decoding
     */
    public SearchResponse searchPeople(SearchRequest request) {
        List<PersonResult> personResults = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(getSearchApiUrl());

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (compatible; TorreSearchBot/1.0)");

            String jsonPayload = objectMapper.writeValueAsString(request);
            httpPost.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));
            logger.debug("Request payload to Torre: {}", jsonPayload);

            httpClient.execute(httpPost, response -> {
                logger.debug("Received response status from Torre: {} {}", response.getCode(), response.getReasonPhrase());

                if (response.getCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                        String line;
                        int lineNumber = 0;
                        while ((line = reader.readLine()) != null) {
                            lineNumber++;
                            if (!line.trim().isEmpty()) {
                                logger.debug("Processing line {}: {}", lineNumber, line);
                                try {
                                    JsonNode node = objectMapper.readTree(line);
                                    logger.debug("Parsed Torre.ai response node at line {}", lineNumber);
                                    
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
                                        logger.debug("Added person: {} (ID: {}, Username: {})", person.getName(), person.getId(), username);
                                    } else {
                                        logger.debug("Line {} is not a valid person result", lineNumber);
                                    }
                                } catch (JsonProcessingException e) {
                                    logger.warn("Error parsing JSON line {}: {}", lineNumber, e.getMessage());
                                }
                            } else {
                                logger.debug("Skipped empty line {}", lineNumber);
                            }
                        }
                        logger.debug("Finished processing stream. Total persons found: {}", personResults.size());
                    }
                } else {
                    String responseBody = "";
                    if (response.getEntity() != null) {
                        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                            responseBody = errorReader.lines().collect(Collectors.joining("\n"));
                        }
                    }
                    throw new ExternalServiceException("Torre API returned error: " + response.getCode() + " - " + response.getReasonPhrase() + " - " + responseBody);
                }
                return null;
            });
        } catch (Exception e) {
            throw new ExternalServiceException("Exception during people search: " + e.getMessage(), e);
        }

        return new SearchResponse(personResults);
    }
}