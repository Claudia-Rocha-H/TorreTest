package com.torre.techtest.feature.search;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.torre.techtest.exception.ExternalServiceException;
import com.torre.techtest.exception.GlobalExceptionHandler;

@WebMvcTest(SearchController.class)
@ContextConfiguration(classes = {SearchController.class, GlobalExceptionHandler.class})
class SearchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchService searchService;

    @Test
    void searchPeople() throws Exception {
        SearchResponse.PersonResult person = new SearchResponse.PersonResult(
            "gg-1",
            "Ana Ruiz",
            "Senior Java Engineer",
            "https://img.local/ana.png",
            "ana-ruiz"
        );
        SearchResponse expected = new SearchResponse(List.of(person));

        when(searchService.searchPeople(any(SearchRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/search/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"java\",\"limit\":30}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].name").value("Ana Ruiz"))
            .andExpect(jsonPath("$.pagination.totalResults").value(1));

        verify(searchService).searchPeople(any(SearchRequest.class));
    }

    @Test
    void blankQuery() throws Exception {
        mockMvc.perform(post("/api/search/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"   \",\"limit\":30}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Search query cannot be empty."));

        verifyNoInteractions(searchService);
    }

    @Test
    void serviceError() throws Exception {
        when(searchService.searchPeople(any(SearchRequest.class)))
            .thenThrow(new ExternalServiceException("upstream failure"));

        mockMvc.perform(post("/api/search/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"java\",\"limit\":30}"))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.message").value("upstream failure"));
    }

    @Test
    void nullQuery() throws Exception {
        mockMvc.perform(post("/api/search/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"limit\":30}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Search query cannot be empty."));
    }

    @Test
    void defaultLimit() throws Exception {
        SearchResponse.PersonResult person = new SearchResponse.PersonResult(
            "gg-1", "Ana Ruiz", "Senior Java Engineer", "https://img.local/ana.png", "ana-ruiz"
        );
        SearchResponse expected = new SearchResponse(List.of(person));

        when(searchService.searchPeople(any(SearchRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/search/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"java\"}"))
            .andExpect(status().isOk());

        verify(searchService).searchPeople(any(SearchRequest.class));
    }
}
