package com.torre.techtest.feature.search;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.torre.techtest.exception.ExternalServiceException;
import com.torre.techtest.exception.GlobalExceptionHandler;

@WebMvcTest(AnalysisController.class)
@ContextConfiguration(classes = {AnalysisController.class, GlobalExceptionHandler.class})
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalysisService analysisService;

    @Test
    void compensation() throws Exception {
        SkillCompensationResponse response = new SkillCompensationResponse("java");
        response.setAverageCompensation(20000.0);
        response.setMedianCompensation(24000.0);
        response.setMinCompensation(16000.0);
        response.setMaxCompensation(40000.0);
        response.setDataPoints(10);

        when(analysisService.analyzeSkillCompensation(eq("java"))).thenReturn(response);

        mockMvc.perform(get("/api/analyze/skill-compensation")
                .param("skill", "java")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.skill").value("java"))
            .andExpect(jsonPath("$.averageCompensation").value(20000.0));

        verify(analysisService).analyzeSkillCompensation("java");
    }

    @Test
    void distribution() throws Exception {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("java");
        response.setTotalProfiles(5);
        response.setDistribution(java.util.List.of(new SkillDistributionResponse.ProficiencyLevel("expert", 100, 5, null)));

        when(analysisService.getSkillProficiencyDistribution(eq("java"))).thenReturn(response);

        mockMvc.perform(get("/api/analyze/skill-distribution")
                .param("skill", "java")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.skill").value("java"))
            .andExpect(jsonPath("$.totalProfiles").value(5));

        verify(analysisService).getSkillProficiencyDistribution("java");
    }

    @Test
    void compensationError() throws Exception {
        when(analysisService.analyzeSkillCompensation(eq("java")))
            .thenThrow(new ExternalServiceException("downstream failure"));

        mockMvc.perform(get("/api/analyze/skill-compensation")
                .param("skill", "java")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.message").value("downstream failure"));
    }

    @Test
    void distributionError() throws Exception {
        when(analysisService.getSkillProficiencyDistribution(eq("java")))
            .thenThrow(new ExternalServiceException("distribution service error"));

        mockMvc.perform(get("/api/analyze/skill-distribution")
                .param("skill", "java")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.message").value("distribution service error"));
    }

    @Test
    void nullSkill() throws Exception {
        when(analysisService.analyzeSkillCompensation(null))
            .thenThrow(new IllegalArgumentException("Skill cannot be empty"));

        mockMvc.perform(get("/api/analyze/skill-compensation")
                .param("skill", "")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void emptyDistribution() throws Exception {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("java");
        response.setTotalProfiles(0);
        response.setDistribution(java.util.List.of());

        when(analysisService.getSkillProficiencyDistribution(eq("java"))).thenReturn(response);

        mockMvc.perform(get("/api/analyze/skill-distribution")
                .param("skill", "java")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalProfiles").value(0));
    }

    @Test
    void multipleLevels() throws Exception {
        SkillDistributionResponse response = new SkillDistributionResponse();
        response.setSkill("java");
        response.setTotalProfiles(100);
        response.setDistribution(java.util.List.of(
            new SkillDistributionResponse.ProficiencyLevel("beginner", 25, 25, null),
            new SkillDistributionResponse.ProficiencyLevel("intermediate", 50, 50, null),
            new SkillDistributionResponse.ProficiencyLevel("expert", 25, 25, null)
        ));

        when(analysisService.getSkillProficiencyDistribution(eq("java"))).thenReturn(response);

        mockMvc.perform(get("/api/analyze/skill-distribution")
                .param("skill", "java")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.distribution.length()").value(3))
            .andExpect(jsonPath("$.distribution[0].level").value("beginner"))
            .andExpect(jsonPath("$.distribution[1].level").value("intermediate"))
            .andExpect(jsonPath("$.distribution[2].level").value("expert"));
    }
}
