package com.torre.techtest.feature.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analyze")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    /**
     * Analyzes skill compensation data using Torre.ai API
     * @param skill The skill to analyze (e.g., "javascript", "python", "java")
     * @return Compensation analysis data including average, min, max, and suggested compensation
     */
    @GetMapping("/skill-compensation")
    public ResponseEntity<SkillCompensationResponse> analyzeSkillCompensation(@RequestParam String skill) {
        try {
            SkillCompensationResponse response = analysisService.analyzeSkillCompensation(skill);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error analyzing skill compensation for " + skill + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Analyzes skill proficiency distribution using Torre.ai API
     * @param skill The skill to analyze (e.g., "javascript", "python", "java")
     * @return Proficiency distribution data including beginner, intermediate, advanced, expert percentages
     */
    @GetMapping("/skill-distribution")
    public ResponseEntity<SkillDistributionResponse> getSkillProficiencyDistribution(@RequestParam String skill) {
        try {
            SkillDistributionResponse response = analysisService.getSkillProficiencyDistribution(skill);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error analyzing skill distribution for " + skill + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
