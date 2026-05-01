package com.torre.techtest.feature.profile;

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
import com.torre.techtest.exception.ResourceNotFoundException;

@WebMvcTest(ProfileController.class)
@ContextConfiguration(classes = {ProfileController.class, GlobalExceptionHandler.class})
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @Test
    void blankUsername() throws Exception {
        mockMvc.perform(get("/api/profile/{username}", "   ")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Username cannot be empty."));
    }

    @Test
    void validUsername() throws Exception {
        PersonDetailsResponse.Person person = new PersonDetailsResponse.Person();
        person.setName("Ana Ruiz");

        PersonDetailsResponse response = new PersonDetailsResponse();
        response.setPerson(person);

        when(profileService.getPersonDetails(eq("ana-ruiz"))).thenReturn(response);

        mockMvc.perform(get("/api/profile/{username}", "ana-ruiz")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.person.name").value("Ana Ruiz"));

        verify(profileService).getPersonDetails("ana-ruiz");
    }

    @Test
    void notFound() throws Exception {
        when(profileService.getPersonDetails(eq("missing"))).thenThrow(new ResourceNotFoundException("profile not found"));

        mockMvc.perform(get("/api/profile/{username}", "missing")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("profile not found"));
    }

    @Test
    void whitespaceUsername() throws Exception {
        mockMvc.perform(get("/api/profile/{username}", "   ")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Username cannot be empty."));
    }

    @Test
    void serviceError() throws Exception {
        when(profileService.getPersonDetails(eq("ana-ruiz")))
            .thenThrow(new ExternalServiceException("Torre.ai API unavailable"));

        mockMvc.perform(get("/api/profile/{username}", "ana-ruiz")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.message").value("Torre.ai API unavailable"));
    }
}
