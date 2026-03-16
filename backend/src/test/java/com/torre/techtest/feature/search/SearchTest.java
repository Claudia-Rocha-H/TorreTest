package com.torre.techtest.feature.search;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SearchTest {

    @Mock
    private SearchService searchService;

    @Test
    void shouldSearchPeople() throws Exception {
        SearchController controller = new SearchController(searchService);

        SearchResponse.PersonResult person = new SearchResponse.PersonResult(
            "gg-1",
            "Ana Ruiz",
            "Senior Java Engineer",
            "https://img.local/ana.png",
            "ana-ruiz"
        );
        SearchResponse expected = new SearchResponse(List.of(person));

        when(searchService.searchPeople(org.mockito.ArgumentMatchers.any(SearchRequest.class))).thenReturn(expected);

        ResponseEntity<?> response = controller.searchPeople(Map.of("query", "java", "limit", "30"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(SearchResponse.class, response.getBody());

        SearchResponse body = (SearchResponse) response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getResults().size());
        assertEquals("Ana Ruiz", body.getResults().get(0).getName());

        ArgumentCaptor<SearchRequest> captor = ArgumentCaptor.forClass(SearchRequest.class);
        verify(searchService).searchPeople(captor.capture());

        SearchRequest sent = captor.getValue();
        assertEquals("java", sent.getQuery());
        assertEquals(30, sent.getLimit());
        assertEquals("person", sent.getIdentityType());
        assertEquals(true, sent.getMeta());
    }
}
