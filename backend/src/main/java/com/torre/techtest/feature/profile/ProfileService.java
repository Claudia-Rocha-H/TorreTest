package com.torre.techtest.feature.profile;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torre.techtest.utils.HtmlUtils;

/**
 * Service for retrieving person profiles from Torre.ai API.
 */
@Service
public class ProfileService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private static final String TORRE_API_BASE_URL = "https://torre.ai/api/genome/bios/";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0";
    private static final String ACCEPT = "application/json, text/plain, */*";
    private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.9";
    private static final String ACCEPT_ENCODING = "gzip, deflate, br";
    
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public ProfileService() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Retrieves profile details for a Torre.ai user.
     * 
     * @param username Torre.ai username/publicId
     * @return PersonDetailsResponse with profile information
     * @throws Exception if API call fails or response parsing fails
     */
    public PersonDetailsResponse getPersonDetails(String username) throws Exception {
        logger.info("Fetching profile details for username: {}", username);
        
        String profileUrl = TORRE_API_BASE_URL + username;
        HttpGet httpGet = new HttpGet(profileUrl);
        
        httpGet.setHeader("Content-Type", CONTENT_TYPE);
        httpGet.setHeader("User-Agent", USER_AGENT);
        httpGet.setHeader("Accept", ACCEPT);
        httpGet.setHeader("Accept-Language", ACCEPT_LANGUAGE);
        httpGet.setHeader("Accept-Encoding", ACCEPT_ENCODING);
        
        try {
            ClassicHttpResponse response = httpClient.executeOpen(null, httpGet, null);
            int statusCode = response.getCode();
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            
            logger.debug("Torre.ai profile API response status: {}", statusCode);
            
            if (statusCode != 200) {
                String errorMessage = String.format(
                    "Torre.ai profile API returned status %d for username '%s': %s", 
                    statusCode, username, responseBody
                );
                logger.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            
            try {
                PersonDetailsResponse profileDetails = objectMapper.readValue(responseBody, PersonDetailsResponse.class);
                
                decodeHtmlEntitiesInProfile(profileDetails);
                
                logger.info("Successfully retrieved profile for username: {}", username);
                return profileDetails;
                
            } catch (Exception parseException) {
                String errorMessage = String.format(
                    "Failed to parse Torre.ai profile response for username '%s': %s", 
                    username, parseException.getMessage()
                );
                logger.error(errorMessage, parseException);
                throw new RuntimeException(errorMessage, parseException);
            }
            
        } catch (Exception httpException) {
            String errorMessage = String.format(
                "HTTP request failed for Torre.ai profile username '%s': %s", 
                username, httpException.getMessage()
            );
            logger.error(errorMessage, httpException);
            throw new RuntimeException(errorMessage, httpException);
        }
    }
    
    /**
     * Decodes HTML entities in profile text fields.
     * 
     * @param profileDetails Profile response to process
     */
    private void decodeHtmlEntitiesInProfile(PersonDetailsResponse profileDetails) {
        if (profileDetails == null) {
            return;
        }
        
        if (profileDetails.getPerson() != null) {
            PersonDetailsResponse.Person person = profileDetails.getPerson();
            person.setName(HtmlUtils.safeDecodeHtmlEntities(person.getName()));
            person.setProfessionalHeadline(HtmlUtils.safeDecodeHtmlEntities(person.getProfessionalHeadline()));
            person.setSummaryOfBio(HtmlUtils.safeDecodeHtmlEntities(person.getSummaryOfBio()));
            person.setPublicId(HtmlUtils.safeDecodeHtmlEntities(person.getPublicId()));
        }
        
        if (profileDetails.getStrengths() != null) {
            for (PersonDetailsResponse.Skill skill : profileDetails.getStrengths()) {
                if (skill != null) {
                    skill.setName(HtmlUtils.safeDecodeHtmlEntities(skill.getName()));
                    skill.setExperience(HtmlUtils.safeDecodeHtmlEntities(skill.getExperience()));
                    skill.setProficiency(HtmlUtils.safeDecodeHtmlEntities(skill.getProficiency()));
                }
            }
        }
        
        if (profileDetails.getExperiences() != null) {
            for (PersonDetailsResponse.Experience experience : profileDetails.getExperiences()) {
                if (experience != null) {
                    experience.setName(HtmlUtils.safeDecodeHtmlEntities(experience.getName()));
                    
                    if (experience.getOrganizations() != null) {
                        for (PersonDetailsResponse.Organization org : experience.getOrganizations()) {
                            if (org != null) {
                                org.setName(HtmlUtils.safeDecodeHtmlEntities(org.getName()));
                                org.setAbout(HtmlUtils.safeDecodeHtmlEntities(org.getAbout()));
                            }
                        }
                    }
                }
            }
        }
        
        if (profileDetails.getEducation() != null) {
            for (PersonDetailsResponse.Education education : profileDetails.getEducation()) {
                if (education != null) {
                    education.setName(HtmlUtils.safeDecodeHtmlEntities(education.getName()));
                    
                    if (education.getOrganizations() != null) {
                        for (PersonDetailsResponse.Organization org : education.getOrganizations()) {
                            if (org != null) {
                                org.setName(HtmlUtils.safeDecodeHtmlEntities(org.getName()));
                                org.setAbout(HtmlUtils.safeDecodeHtmlEntities(org.getAbout()));
                            }
                        }
                    }
                }
            }
        }
    }
}
