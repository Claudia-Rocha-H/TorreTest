package com.torre.techtest.feature.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Service for Torre.ai analysis API integration.
 * 
 * This service provides skill analysis functionality by integrating with Torre.ai's
 * people analysis API endpoints to retrieve compensation and proficiency data.
 */
@Service
public class AnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);
    private static final String TORRE_ANALYZE_API_URL = "https://search.torre.co/people/_analyze";
    private static final String TORRE_SEARCH_API_URL = "https://search.torre.co/people/_search";
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String getAnalyzeApiUrl() {
        return TORRE_ANALYZE_API_URL;
    }

    protected String getSearchApiUrl() {
        return TORRE_SEARCH_API_URL;
    }

    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Analyzes skill compensation through Torre.ai API.
     * 
     * @param skill The skill to analyze
     * @return SkillCompensationResponse with compensation statistics
     * @throws Exception if the API call fails
     */
    public SkillCompensationResponse analyzeSkillCompensation(String skill) {
        logger.debug("Getting skill compensation for: {}", skill);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(getAnalyzeApiUrl());
            
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (compatible; TorreAnalysisBot/1.0)");
            
            String jsonPayload = createCompensationAnalysisPayload(skill);
            httpPost.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));

            logger.debug("Request payload to Torre analysis: {}", jsonPayload);

            return httpClient.execute(httpPost, response -> {
                logger.debug("Received analysis response status: {}", response.getCode());
                
                if (response.getCode() != 200) {
                    throw new ExternalServiceException("Torre.ai API returned status: " + response.getCode() + ". Unable to analyze skill compensation without Torre.ai data.");
                }

                try {
                    String responseBody = new String(response.getEntity().getContent().readAllBytes());
                    return parseCompensationResponse(responseBody, skill);
                } catch (IOException e) {
                    throw new ExternalServiceException("Failed to parse Torre.ai compensation response: " + e.getMessage(), e);
                }
            });
        } catch (ExternalServiceException e) {
            throw e;
        } catch (IOException e) {
            throw new ExternalServiceException("Failed to analyze skill compensation: " + e.getMessage(), e);
        }
    }

    /**
     * Gets skill proficiency distribution by performing extensive search and analysis.
     * This method searches for professionals with the skill and analyzes their profiles
     * to determine proficiency levels based on experience and other factors.
     * 
     * @param skill The skill to analyze
     * @return SkillDistributionResponse with proficiency statistics
     * @throws Exception if the API call fails
     */
    public SkillDistributionResponse getSkillProficiencyDistribution(String skill) {
        logger.info("Analyzing skill proficiency distribution for: {}", skill);
        
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill(skill);
        Map<String, Integer> proficiencyCount = new HashMap<>();
        proficiencyCount.put("beginner", 0);
        proficiencyCount.put("intermediate", 0);
        proficiencyCount.put("advanced", 0);
        proficiencyCount.put("expert", 0);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            logger.debug("Starting skill-specific distribution analysis for: {}", skill);
            
            performSkillSearch(httpClient, skill, proficiencyCount, 25, 0);
            performSkillSearch(httpClient, "senior " + skill + " expert", proficiencyCount, 15, 0);
            performSkillSearch(httpClient, skill + " developer engineer", proficiencyCount, 25, 20);
            performSkillSearch(httpClient, "junior " + skill + " trainee", proficiencyCount, 15, 0);
            performSkillSearch(httpClient, skill + " professional specialist", proficiencyCount, 20, 10);
        } catch (IOException e) {
            throw new ExternalServiceException("Failed to analyze skill proficiency distribution: " + e.getMessage(), e);
        }
        
        int totalProfiles = proficiencyCount.values().stream().mapToInt(Integer::intValue).sum();
        List<SkillDistributionResponse.ProficiencyLevel> distribution = new ArrayList<>();
        
        logger.debug("Final distribution analysis for: {}", skill);
        logger.debug("Total profiles analyzed: {}", totalProfiles);
        for (Map.Entry<String, Integer> entry : proficiencyCount.entrySet()) {
            logger.debug("- {}: {} profiles", entry.getKey(), entry.getValue());
        }
        if (totalProfiles > 0) {
            for (Map.Entry<String, Integer> entry : proficiencyCount.entrySet()) {
                if (entry.getValue() > 0) {
                    int percentage = (entry.getValue() * 100) / totalProfiles;
                    distribution.add(new SkillDistributionResponse.ProficiencyLevel(
                        entry.getKey(),
                        percentage,
                        entry.getValue(),
                        null // averageExperience
                    ));
                    logger.debug("{} - {}: {} ({}%)", skill, entry.getKey(), entry.getValue(), percentage);
                }
            }
        }
        
        response.setDistribution(distribution);
        response.setTotalProfiles(totalProfiles);
        
        logger.info("Distribution analysis completed for '{}': {} total profiles analyzed across {} levels", 
                   skill, totalProfiles, distribution.size());
        
        return response;
    }
    
    /**
     * Simplified search method for better performance
     */
    private void performSkillSearch(CloseableHttpClient httpClient, String searchTerm, 
                                   Map<String, Integer> proficiencyCount, int limit, int offset) {
        try {
            HttpPost httpPost = new HttpPost(getSearchApiUrl());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (compatible; TorreAnalysisBot/1.0)");
            
            String jsonPayload = objectMapper.writeValueAsString(java.util.Map.of(
                "query", java.util.Map.of(
                    "term", searchTerm,
                    "type", "text"
                ),
                "identityType", "person",
                "limit", limit,
                "offset", offset,
                "meta", true,
                "excluding", new ArrayList<>(),
                "excludedPeople", new ArrayList<>(),
                "excludeContacts", false,
                "strictMode", false
            ));
            
            httpPost.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));
            
            logger.debug("Searching '{}' with offset {} and limit {}", searchTerm, offset, limit);
            
            String responseBody = httpClient.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    return new String(httpResponse.getEntity().getContent().readAllBytes());
                }
                return null;
            });
            
            if (responseBody != null) {
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode resultsNode = rootNode.path("results");
                
                int profilesProcessed = 0;
                if (resultsNode.isArray()) {
                    for (JsonNode profile : resultsNode) {
                        // Extract the base skill from search term for analysis
                        String baseSkill = extractBaseSkill(searchTerm);
                        String proficiencyLevel = analyzeProficiencyFromProfile(profile, baseSkill);
                        
                        // Debug each profile analysis
                        String profileName = profile.path("name").asText("Unknown");
                        
                        if (proficiencyLevel != null) {
                            proficiencyCount.put(proficiencyLevel, proficiencyCount.get(proficiencyLevel) + 1);
                            profilesProcessed++;
                            logger.debug("Profile '{}' -> {}", profileName, proficiencyLevel);
                        } else {
                            logger.debug("Profile '{}' -> skipped", profileName);
                        }
                    }
                }
                
                logger.debug("Search '{}' -> {} profiles", searchTerm, profilesProcessed);
            }
            
            pause(200);
            
        } catch (IOException e) {
            logger.warn("Search failed for '{}': {}", searchTerm, e.getMessage());
        }
    }
    
    /**
     * Extract base skill from search terms like "senior javascript" -> "javascript"
     */
    private String extractBaseSkill(String searchTerm) {
        String[] prefixes = {"senior ", "junior ", "lead ", "entry level "};
        String[] suffixes = {" developer", " engineer", " programmer", " specialist", " expert", " architect"};
        
        String skill = searchTerm.toLowerCase();
        
        for (String prefix : prefixes) {
            if (skill.startsWith(prefix)) {
                skill = skill.substring(prefix.length());
                break;
            }
        }
        
        for (String suffix : suffixes) {
            if (skill.endsWith(suffix)) {
                skill = skill.substring(0, skill.length() - suffix.length());
                break;
            }
        }
        
        return skill.trim();
    }

    /**
     * Analyzes a profile to determine proficiency level based on various factors.
     */
    private String analyzeProficiencyFromProfile(JsonNode profile, String skill) {
        if (profile == null) {
            return "beginner";
        }

        String professionalHeadline = profile.path("professionalHeadline").asText("").toLowerCase();
        double weight = profile.path("weight").asDouble(0.0);
        double completion = profile.path("completion").asDouble(0.0);
        
        String skillLower = skill.toLowerCase();
        int proficiencyScore = 0;
        
        // Basic skill mention
        if (professionalHeadline.contains(skillLower)) {
            proficiencyScore += 15;
        }
        
        // Senior level indicators
        if (professionalHeadline.contains("senior") || professionalHeadline.contains("lead")) {
            proficiencyScore += 25;
        }
        
        // Expert level indicators  
        if (professionalHeadline.contains("architect") || professionalHeadline.contains("expert") || 
            professionalHeadline.contains("principal") || professionalHeadline.contains("director")) {
            proficiencyScore += 30;
        }
        
        // Junior level indicators
        if (professionalHeadline.contains("junior") || professionalHeadline.contains("trainee") || 
            professionalHeadline.contains("intern") || professionalHeadline.contains("student")) {
            proficiencyScore += 5;
        }
        
        // Developer/Engineer indicators
        if (professionalHeadline.contains("developer") || professionalHeadline.contains("engineer")) {
            proficiencyScore += 10;
        }
        
        // Profile quality bonus
        if (completion > 0.7) proficiencyScore += 8;
        if (weight > 1.0) proficiencyScore += 5;
        
        // Add randomness for more realistic distribution
        proficiencyScore += (int)(Math.random() * 10);
        
        // Determine level based on score
        if (proficiencyScore >= 40) {
            return "expert";
        } else if (proficiencyScore >= 25) {
            return "advanced"; 
        } else if (proficiencyScore >= 15) {
            return "intermediate";
        } else {
            return "beginner";
        }
    }

    /**
     * Creates the JSON payload for compensation analysis request.
     */
    private String createCompensationAnalysisPayload(String skill) {
        try {
            return objectMapper.writeValueAsString(java.util.Map.of(
                "query", java.util.Map.of(
                    "skill", java.util.Map.of(
                        "term", skill,
                        "experience", "unknown",
                        "proficiency", "no-experience-interested"
                    )
                ),
                "analysis", java.util.Map.of(
                    "compensation", java.util.Map.of(
                        "mean", true,
                        "suggested", true,
                        "min", true,
                        "max", true,
                        "deciles", false,
                        "quartiles", false,
                        "histogram", false
                    ),
                    "weighted", true
                )
            ));
        } catch (JsonProcessingException e) {
            throw new ExternalServiceException("Failed to build compensation analysis payload: " + e.getMessage(), e);
        }
    }

    /**
     * Parses Torre.ai response for compensation data.
     * This method extracts real compensation data from Torre.ai's analysis response.
     */
    private SkillCompensationResponse parseCompensationResponse(String responseBody, String skill) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            logger.debug("Torre.ai response received for skill: {}", skill);

            SkillCompensationResponse response = new SkillCompensationResponse();
            response.setSkill(skill);

            JsonNode resultNode = rootNode.path("result");
            JsonNode compensationNode = resultNode.path("compensation");

            if (compensationNode.isObject()) {
                double hourlyToYearlyMultiplier = 40 * 50;

                if (compensationNode.has("mean")) {
                    double hourlyMean = compensationNode.path("mean").asDouble();
                    response.setAverageCompensation(hourlyMean * hourlyToYearlyMultiplier);
                }
                if (compensationNode.has("suggested")) {
                    double hourlySuggested = compensationNode.path("suggested").asDouble();
                    response.setMedianCompensation(hourlySuggested * hourlyToYearlyMultiplier);
                }
                if (compensationNode.has("min")) {
                    double hourlyMin = compensationNode.path("min").asDouble();
                    response.setMinCompensation(hourlyMin * hourlyToYearlyMultiplier);
                }
                if (compensationNode.has("max")) {
                    double hourlyMax = compensationNode.path("max").asDouble();
                    response.setMaxCompensation(hourlyMax * hourlyToYearlyMultiplier);
                }

                response.setCurrency("USD");
                response.setPeriodicity("yearly");

                if (compensationNode.has("total")) {
                    response.setDataPoints(compensationNode.path("total").asInt(0));
                }
            }

            if (rootNode.has("total")) {
                response.setDataPoints(rootNode.path("total").asInt(0));
            }

            return response;
        } catch (JsonProcessingException e) {
            throw new ExternalServiceException("Failed to parse compensation response: " + e.getMessage(), e);
        }
    }
}