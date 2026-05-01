package com.torre.techtest.feature.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.torre.techtest.exception.ExternalServiceException;
import com.torre.techtest.exception.ResourceNotFoundException;

class ProfileServiceTest {

    @RegisterExtension
    static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();

    @Test
    void decodeEntities() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/ana-ruiz"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{" +
                    "\"person\":{\"name\":\"Ana &amp; Ruiz\",\"professionalHeadline\":\"Senior Java Engineer\",\"summaryOfBio\":\"Loves &lt;code&gt;\",\"publicId\":\"ana-&amp;ruiz\"}," +
                    "\"strengths\":[{\"name\":\"Java &amp; Spring\",\"experience\":\"5 &amp; years\",\"proficiency\":\"expert\"}]," +
                    "\"experiences\":[{\"name\":\"Backend &amp; Platform\",\"organizations\":[{\"name\":\"Torre &amp; Co\",\"about\":\"Builds &lt;systems&gt;\"}]}]," +
                    "\"education\":[{\"name\":\"Computer &amp; Science\",\"organizations\":[{\"name\":\"University &amp; Lab\",\"about\":\"Research &amp; dev\"}]}]" +
                    "}")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());
        PersonDetailsResponse response = service.getPersonDetails("ana-ruiz");

        assertNotNull(response);
        assertEquals("Ana & Ruiz", response.getPerson().getName());
        assertEquals("Loves <code>", response.getPerson().getSummaryOfBio());
        assertEquals("Java & Spring", response.getStrengths().get(0).getName());
        assertEquals("Torre & Co", response.getExperiences().get(0).getOrganizations().get(0).getName());
        assertEquals("University & Lab", response.getEducation().get(0).getOrganizations().get(0).getName());
    }

    @Test
    void notFound() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/missing"))
            .willReturn(aResponse().withStatus(404).withBody("not found")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getPersonDetails("missing")
        );

        assertEquals("Torre.ai profile API returned status 404 for username 'missing': not found", exception.getMessage());
    }

    @Test
    void error500() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/ana-ruiz"))
            .willReturn(aResponse().withStatus(500).withBody("server error")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.getPersonDetails("ana-ruiz")
        );

        assertTrue(exception.getMessage().contains("Torre.ai profile API returned status 500"));
    }

    @Test
    void error503() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/ana-ruiz"))
            .willReturn(aResponse().withStatus(503).withBody("service unavailable")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.getPersonDetails("ana-ruiz")
        );

        assertTrue(exception.getMessage().contains("Torre.ai profile API returned status 503"));
    }

    @Test
    void malformedJson() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/ana-ruiz"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{ invalid json")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.getPersonDetails("ana-ruiz")
        );

        assertTrue(exception.getMessage().contains("Failed to parse Torre.ai profile response"));
    }

    @Test
    void decodeNested() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/test-user"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{" +
                    "\"person\":{\"name\":\"John &quot;Johnny&quot; Doe\",\"professionalHeadline\":\"Developer &amp; Architect\"}," +
                    "\"strengths\":[{\"name\":\"C++ &amp; Java\"}]," +
                    "\"experiences\":[{\"organizations\":[{\"name\":\"Tech &amp; Co\"}]}]," +
                    "\"education\":[{\"organizations\":[{\"name\":\"MIT &amp; Harvard\"}]}]" +
                    "}")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());
        PersonDetailsResponse response = service.getPersonDetails("test-user");

        assertNotNull(response);
        assertEquals("John \"Johnny\" Doe", response.getPerson().getName());
        assertEquals("Developer & Architect", response.getPerson().getProfessionalHeadline());
    }

    @Test
    void emptyProfile() {
        wireMockServer.stubFor(get(urlEqualTo("/api/genome/bios/minimal"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"person\":{\"name\":\"Minimal\"}}")));

        ProfileService service = new TestProfileService(wireMockServer.baseUrl());
        PersonDetailsResponse response = service.getPersonDetails("minimal");

        assertNotNull(response);
        assertEquals("Minimal", response.getPerson().getName());
    }

    private static final class TestProfileService extends ProfileService {
        private final String baseUrl;

        private TestProfileService(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Override
        protected String getProfileBaseUrl() {
            return baseUrl + "/api/genome/bios/";
        }
    }
}
