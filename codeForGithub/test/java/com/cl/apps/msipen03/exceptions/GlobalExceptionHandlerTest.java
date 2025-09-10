package com.cl.apps.msipen03.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleRemittanceNotFoundException_ShouldReturnNotFoundStatus() {
        RemittanceNotFoundException ex = new RemittanceNotFoundException("Remittance operation not found for ID:");

        ResponseEntity<Object> response = handler.handleRemittanceNotFoundException(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 404)
                .containsEntry("error", "Remittance order not found");
    }

    @Test
    void handleDataAccessException_ShouldReturnInternalServerError() {
        DataAccessException ex = mock(DataAccessException.class);

        ResponseEntity<Object> response = handler.handleDataAccess(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 500)
                .containsEntry("error", "Database Error")
                .containsEntry("message", "A database error occurred. Please try again later.");
    }

    @Test
    void handleJpaSystemException_ShouldReturnInternalServerError() {
        JpaSystemException ex = mock(JpaSystemException.class);

        ResponseEntity<Object> response = handler.handleDataAccess(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 500)
                .containsEntry("error", "Database Error");
    }

    @Test
    void handleMethodArgumentTypeMismatch_ShouldReturnBadRequest() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        ResponseEntity<Object> response = handler.handleBadRequest(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 400)
                .containsEntry("error", "Bad Request");
    }

    @Test
    void handleMissingServletRequestParameter_ShouldReturnBadRequest() {
        MissingServletRequestParameterException ex = mock(MissingServletRequestParameterException.class);

        ResponseEntity<Object> response = handler.handleBadRequest(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 400)
                .containsEntry("error", "Bad Request");
    }

    @Test
    void handleHttpMessageNotReadable_ShouldReturnBadRequest() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        ResponseEntity<Object> response = handler.handleBadRequest(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 400)
                .containsEntry("error", "Bad Request");
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<Object> response = handler.handleGenericException(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body)
                .containsKey("timestamp")
                .containsEntry("status", 500)
                .containsEntry("error", "Internal Server Error")
                .containsEntry("message", "An unexpected error occurred");
    }

    @Test
    void responseShouldContainValidTimestamp() {
        Exception ex = new RuntimeException("Test error");

        ResponseEntity<Object> response = handler.handleGenericException(ex);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(body.get("timestamp"))
                .isNotNull()
                .isInstanceOf(ZonedDateTime.class);
    }
}