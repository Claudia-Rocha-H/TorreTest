package com.torre.techtest.feature.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ProfileControllerTest {

    @Test
    void badRequestOnBlankUsername() {
        ProfileController controller = new ProfileController(null);

        ResponseEntity<PersonDetailsResponse> response = controller.getPersonProfile("   ");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void okOnValidUsername() {
        ProfileService fakeService = new ProfileService() {
            @Override
            public PersonDetailsResponse getPersonDetails(String username) {
                PersonDetailsResponse.Person person = new PersonDetailsResponse.Person();
                person.setName("Ana Ruiz");

                PersonDetailsResponse response = new PersonDetailsResponse();
                response.setPerson(person);
                return response;
            }
        };

        ProfileController controller = new ProfileController(fakeService);

        ResponseEntity<PersonDetailsResponse> response = controller.getPersonProfile("ana-ruiz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ana Ruiz", response.getBody().getPerson().getName());
    }
}
