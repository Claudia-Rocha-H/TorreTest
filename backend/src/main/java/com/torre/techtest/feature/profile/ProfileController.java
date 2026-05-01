package com.torre.techtest.feature.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Torre.ai profile operations
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Retrieves Torre.ai profile details
     */
    @GetMapping("/{username}")
    public ResponseEntity<PersonDetailsResponse> getPersonProfile(@PathVariable String username) {
        logger.info("Received request for profile details of username: {}", username);

        if (username == null || username.trim().isEmpty()) {
            logger.warn("Invalid username provided: {}", username);
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        PersonDetailsResponse profileDetails = profileService.getPersonDetails(username.trim());

        logger.info("Successfully retrieved profile for username: {}", username);
        return ResponseEntity.ok(profileDetails);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Profile service is operational");
    }
}