package com.torre.techtest.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    @Test
    void badRequest400() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test/bad-request");

        IllegalArgumentException exception = new IllegalArgumentException("invalid input");

        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequest(exception, request);
        ApiErrorResponse body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("invalid input", body.message());
        assertEquals("/api/test/bad-request", body.path());
    }

    @Test
    void notFound404() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test/not-found");

        ResourceNotFoundException exception = new ResourceNotFoundException("profile missing");

        ResponseEntity<ApiErrorResponse> response = handler.handleNotFound(exception, request);
        ApiErrorResponse body = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("Not Found", body.error());
        assertEquals("profile missing", body.message());
        assertEquals("/api/test/not-found", body.path());
    }

    @Test
    void badGateway502() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test/external");

        ExternalServiceException exception = new ExternalServiceException("upstream timeout");

        ResponseEntity<ApiErrorResponse> response = handler.handleExternalService(exception, request);
        ApiErrorResponse body = response.getBody();

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertNotNull(body);
        assertEquals(502, body.status());
        assertEquals("Bad Gateway", body.error());
        assertEquals("upstream timeout", body.message());
        assertEquals("/api/test/external", body.path());
    }

    @Test
    void unexpected500() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test/unexpected");

        RuntimeException exception = new RuntimeException("unexpected boom");

        ResponseEntity<ApiErrorResponse> response = handler.handleUnexpected(exception, request);
        ApiErrorResponse body = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals("Internal Server Error", body.error());
        assertEquals("unexpected boom", body.message());
        assertEquals("/api/test/unexpected", body.path());
    }
}
