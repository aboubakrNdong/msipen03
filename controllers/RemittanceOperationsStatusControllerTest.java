package com.cl.msipen03.controllers;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.entities.RemittanceOperationsPaginationResponse;
import com.cl.msipen03.exceptions.InternalMSIPEN03Exception;
import com.cl.msipen03.facades.RemittanceOperationsFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class RemittanceOperationsStatusControllerTest {

    @Mock
    private RemittanceOperationsFacade remittanceOperationsFacade;

    @InjectMocks
    private RemittanceOperationsStatusController controller;

    private RemittanceOperationsPaginationResponse mockResponse;

    @BeforeEach
    void setUp() {
        List<RemittanceOperationRequest> operations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 2L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 2L)
        );

        mockResponse = RemittanceOperationsPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(2L)
                .list(operations)
                .build();
    }

    @Test
    void testShouldReturnOperationsList() throws InternalMSIPEN03Exception {
        when(remittanceOperationsFacade.getOperationListResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        ResponseEntity<RemittanceOperationsPaginationResponse> response =
                controller.getOperationList("IPF000000127670", 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().list()).hasSize(2);
        assertThat(response.getBody().total()).isEqualTo(2L);
    }

    @Test
    void testShouldHandleInternalException() {
        when(remittanceOperationsFacade.getOperationListResponse(anyString(), anyInt(), anyInt()))
                .thenThrow(new InternalMSIPEN03Exception("Database error"));

        assertThatThrownBy(() ->
                controller.getOperationList("IPF000000127670", 0, 10))
                .isInstanceOf(InternalMSIPEN03Exception.class)
                .hasMessage("Database error");
    }

    @Test
    void testShouldReturnEmptyList() throws InternalMSIPEN03Exception {
        RemittanceOperationsPaginationResponse emptyResponse = RemittanceOperationsPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(0L)
                .list(List.of())
                .build();

        when(remittanceOperationsFacade.getOperationListResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyResponse);

        ResponseEntity<RemittanceOperationsPaginationResponse> response =
                controller.getOperationList("IPF000000127670", 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().list()).isEmpty();
        assertThat(response.getBody().total()).isZero();
    }
}