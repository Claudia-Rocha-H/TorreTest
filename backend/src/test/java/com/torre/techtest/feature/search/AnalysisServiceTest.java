package com.torre.techtest.feature.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.torre.techtest.exception.ExternalServiceException;

class AnalysisServiceTest {

    @RegisterExtension
    static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();

    @Test
    void compensation() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_analyze"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"total\":10,\"result\":{\"compensation\":{\"mean\":10,\"suggested\":12,\"min\":8,\"max\":20,\"total\":10}}}")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());
        SkillCompensationResponse response = service.analyzeSkillCompensation("java");

        assertEquals("java", response.getSkill());
        assertEquals(20000.0, response.getAverageCompensation());
        assertEquals(24000.0, response.getMedianCompensation());
        assertEquals(16000.0, response.getMinCompensation());
        assertEquals(40000.0, response.getMaxCompensation());
        assertEquals("USD", response.getCurrency());
        assertEquals("yearly", response.getPeriodicity());
        assertEquals(10, response.getDataPoints());
    }

    @Test
    void distribution() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_search"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"results\":[{\"name\":\"Ana Ruiz\",\"professionalHeadline\":\"Senior Java Developer Expert\",\"weight\":1.5,\"completion\":0.9}]}")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());
        SkillDistributionResponse response = service.getSkillProficiencyDistribution("java");

        assertEquals("java", response.getSkill());
        assertEquals(5, response.getTotalProfiles());
        assertEquals(1, response.getDistribution().size());
        assertEquals("expert", response.getDistribution().get(0).getLevel());
        assertEquals(5, response.getDistribution().get(0).getCount());
    }

    @Test
    void analyzeError() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_analyze"))
            .willReturn(aResponse().withStatus(503).withBody("service unavailable")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.analyzeSkillCompensation("java")
        );

        assertTrue(exception.getMessage().contains("Unable to analyze skill compensation"));
    }

    @Test
    void analyze500() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_analyze"))
            .willReturn(aResponse().withStatus(500).withBody("internal server error")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.analyzeSkillCompensation("java")
        );

        assertTrue(exception.getMessage().contains("Unable to analyze skill compensation"));
    }

    @Test
    void emptyDistribution() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_search"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"results\":[]}")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());
        SkillDistributionResponse response = service.getSkillProficiencyDistribution("nonexistent");

        assertEquals("nonexistent", response.getSkill());
        assertEquals(0, response.getTotalProfiles());
        assertTrue(response.getDistribution().isEmpty());
    }

    @Test
    void nullCompensation() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_analyze"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"total\":0,\"result\":{}}")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());
        SkillCompensationResponse response = service.analyzeSkillCompensation("rare-skill");

        assertEquals("rare-skill", response.getSkill());
        assertEquals(0, response.getDataPoints());
    }

    @Test
    void validDistribution() {
        wireMockServer.stubFor(post(urlEqualTo("/people/_search"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"results\":[{\"name\":\"Ana Ruiz\",\"professionalHeadline\":\"Senior Java Developer Expert\",\"weight\":1.5,\"completion\":0.9}]}")));

        AnalysisService service = new TestAnalysisService(wireMockServer.baseUrl());
        SkillDistributionResponse response = service.getSkillProficiencyDistribution("java");

        assertEquals("java", response.getSkill());
        assertTrue(response.getTotalProfiles() > 0);
    }

    private static final class TestAnalysisService extends AnalysisService {
        private final String baseUrl;

        private TestAnalysisService(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Override
        protected String getAnalyzeApiUrl() {
            return baseUrl + "/people/_analyze";
        }

        @Override
        protected String getSearchApiUrl() {
            return baseUrl + "/people/_search";
        }

        @Override
        protected void pause(long millis) {
        }
    }
}
