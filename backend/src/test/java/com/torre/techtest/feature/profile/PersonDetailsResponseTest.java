package com.torre.techtest.feature.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class PersonDetailsResponseTest {

    @Test
    void shouldExposeNestedModels() {
        PersonDetailsResponse.Location location = new PersonDetailsResponse.Location("Bogota", "BOG", "Colombia", "CO", 1.0, 2.0, "UTC-5", "place-1");
        PersonDetailsResponse.Person person = new PersonDetailsResponse.Person("id-1", "Ana", "Engineer", "pic", "bio", "ana", location);
        PersonDetailsResponse.Skill skill = new PersonDetailsResponse.Skill("skill-1", "Java", "5 years", "expert", 0.9);
        PersonDetailsResponse.Organization organization = new PersonDetailsResponse.Organization(1L, "Torre", "torre", "pic", "theme", "service", "url", "about");
        PersonDetailsResponse.Experience experience = new PersonDetailsResponse.Experience("exp-1", "Backend", List.of(organization), "1", "2020", "12", "2024");
        PersonDetailsResponse.Education education = new PersonDetailsResponse.Education("edu-1", "CS", List.of(organization), "1", "2010", "12", "2014");

        PersonDetailsResponse response = new PersonDetailsResponse(person, List.of(skill), List.of(experience), List.of(education));

        assertEquals("Ana", response.getPerson().getName());
        assertEquals("Java", response.getStrengths().get(0).getName());
        assertEquals("Backend", response.getExperiences().get(0).getName());
        assertEquals("CS", response.getEducation().get(0).getName());
    }
}
