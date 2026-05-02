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

class SearchServiceTest {

    @RegisterExtension
    static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();

    @Test
    void parseStream() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"ggId\":\"gg-1\",\"name\":\"Ana &amp; Ruiz\",\"professionalHeadline\":\"Senior Java Engineer\",\"imageUrl\":\"https://img.local/ana.png\",\"username\":\"ana-ruiz\"}\n\nnot-json\n{\"ggId\":\"gg-2\",\"name\":\"Luis\",\"professionalHeadline\":\"Java Developer\"}\n")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());
        SearchResponse response = service.searchPeople(new SearchRequest("java", 30));

        assertEquals(2, response.getResults().size());
        assertEquals("Ana & Ruiz", response.getResults().get(0).getName());
        assertEquals("ana-ruiz", response.getResults().get(0).getUsername());
        assertEquals("gg-2", response.getResults().get(1).getUsername());
    }

    @Test
    void error500() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(500)
                .withBody("boom")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.searchPeople(new SearchRequest("java", 30))
        );

        assertTrue(exception.getMessage().contains("Exception during people search"));
    }

    @Test
    void emptyStream() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());
        SearchResponse response = service.searchPeople(new SearchRequest("nonexistent", 30));

        assertEquals(0, response.getResults().size());
    }

    @Test
    void malformedJson() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"ggId\":\"gg-1\",\"name\":\"Ana Ruiz\"}\n{invalid json}\n{\"ggId\":\"gg-2\",\"name\":\"Luis\"}\n")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());
        SearchResponse response = service.searchPeople(new SearchRequest("java", 30));

        assertEquals(2, response.getResults().size());
        assertEquals("Ana Ruiz", response.getResults().get(0).getName());
        assertEquals("Luis", response.getResults().get(1).getName());
    }

    @Test
    void error503() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(503)
                .withBody("Service Unavailable")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.searchPeople(new SearchRequest("java", 30))
        );

        assertTrue(exception.getMessage().contains("Exception during people search"));
    }

    @Test
    void htmlDecode() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"ggId\":\"gg-1\",\"name\":\"Ana &amp; Ruiz\",\"professionalHeadline\":\"Senior &lt;code&gt; Engineer\",\"username\":\"ana-ruiz\"}\n")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());
        SearchResponse response = service.searchPeople(new SearchRequest("java", 30));

        assertEquals(1, response.getResults().size());
        assertEquals("Ana & Ruiz", response.getResults().get(0).getName());
        assertEquals("Senior <code> Engineer", response.getResults().get(0).getProfessionalHeadline());
    }

    @Test
    void missingFields() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"ggId\":\"gg-1\",\"name\":\"Ana Ruiz\"}\n")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());
        SearchResponse response = service.searchPeople(new SearchRequest("java", 30));

        assertEquals(1, response.getResults().size());
        assertEquals("Ana Ruiz", response.getResults().get(0).getName());
        assertEquals("gg-1", response.getResults().get(0).getUsername());
    }

    @Test
    void defaultApiUrl() {
        SearchService service = new SearchService();
        assertEquals("https://torre.ai/api/entities/_searchStream", service.getSearchApiUrl());
    }

    @Test
    void non200NoBody() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(502)));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());

        ExternalServiceException exception = assertThrows(
            ExternalServiceException.class,
            () -> service.searchPeople(new SearchRequest("java", 30))
        );

        assertTrue(exception.getMessage().contains("Exception during people search"));
    }

    @Test
    void ignoreNonPersonNode() {
        wireMockServer.stubFor(post(urlEqualTo("/api/entities/_searchStream"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"name\":\"Only Name\"}\n{\"ggId\":\"gg-1\",\"name\":\"Ana Ruiz\"}\n")));

        SearchService service = new TestSearchService(wireMockServer.baseUrl());
        SearchResponse response = service.searchPeople(new SearchRequest("java", 30));

        assertEquals(1, response.getResults().size());
        assertEquals("Ana Ruiz", response.getResults().get(0).getName());
    }

    private static final class TestSearchService extends SearchService {
        private final String baseUrl;

        private TestSearchService(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Override
        protected String getSearchApiUrl() {
            return baseUrl + "/api/entities/_searchStream";
        }
    }
}
