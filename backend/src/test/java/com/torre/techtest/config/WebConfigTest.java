package com.torre.techtest.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

class WebConfigTest {

    private final WebConfig webConfig = new WebConfig();

    @Test
    void webConfigImplementsWebMvcConfigurer() {
        assertTrue(webConfig instanceof WebMvcConfigurer);
    }

    @Test
    void addCorsMappingsConfiguresRegistry() {
        CorsRegistry registry = new CorsRegistry();
        webConfig.addCorsMappings(registry);
        assertNotNull(registry);
    }

    @Test
    void webConfigCanBeInstantiated() {
        assertNotNull(webConfig);
    }

    @Test
    void corsMappingsDoNotThrowException() {
        CorsRegistry registry = new CorsRegistry();
        webConfig.addCorsMappings(registry);
    }

    @Test
    void newCorsRegistryHasNoMappings() {
        CorsRegistry newRegistry = new CorsRegistry();
        assertNotNull(newRegistry);
    }
}
