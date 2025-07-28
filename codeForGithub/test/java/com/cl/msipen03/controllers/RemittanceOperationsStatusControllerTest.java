package com.cl.msipen03.controllers;

import com.cl.msipen03.entities.*;
import com.cl.msipen03.exceptions.InternalMSIPEN03Exception;
import com.cl.msipen03.facades.RemittanceBuildExcelFacade;
import com.cl.msipen03.facades.RemittanceOperationsFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemittanceOperationsStatusControllerTest {

    @Mock
    private RemittanceOperationsFacade remittanceOperationsFacade;

    @Mock
    private RemittanceBuildExcelFacade remittanceBuildExcelFacade;

    @InjectMocks
    private RemittanceOperationsStatusController controller;

    private RemittanceRequestPaginationResponse mockResponse;
    private RemittanceRequest mockRemittanceRequest;
    private ExcelGenerationEntity mockExcelEntity;

    @BeforeEach
    void setUp() {
        Date executionDate = Date.valueOf("2025-07-28");


        mockRemittanceRequest = new RemittanceRequest(
                123456,
                Timestamp.from(Instant.now()),
                "REM000000101000",
                "REJECTED",
                "CONDAT SAS",
                102481,
                2,
                executionDate,
                new BigDecimal("1000.00")
        );

        List<RemittanceOperations> operations = Arrays.asList(
                new RemittanceOperations(
                        "LCL CREDIT LYONNAIS",
                        "30002003870000017010T61",
                        "CRLYFRPPXXX",
                        executionDate,
                        "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                        "PACIFICA2000157390",
                        "REF123",
                        new BigDecimal("500.00"),
                        "EUR",
                        "BNK123",
                        "REJECTED",
                        "Specific transaction/message amount is greater than allowed maximum",
                        2L
                ),
                new RemittanceOperations(
                        "LCL CREDIT LYONNAIS",
                        "30002095050000079135P57",
                        "CRLYFRPPXXX",
                        executionDate,
                        "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                        "PACIFICA2000157390",
                        "REF124",
                        new BigDecimal("500.00"),
                        "EUR",
                        "BNK124",
                        "REJECTED",
                        "Account number is invalid or missing.",
                        2L
                )
        );

        mockResponse = RemittanceRequestPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(2L)
                .request(mockRemittanceRequest)
                .list(operations)
                .build();

        mockExcelEntity = new ExcelGenerationEntity(
                new ByteArrayInputStream(new byte[0]),
                123456
        );
    }


    @Test
    void testShouldReturnOperationsList() throws InternalMSIPEN03Exception {
        when(remittanceOperationsFacade.getOperationListResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        ResponseEntity<RemittanceRequestPaginationResponse> response =
                controller.getOperationList("IPF000000127670", 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().list()).hasSize(2);
        assertThat(response.getBody().total()).isEqualTo(2L);
        assertThat(response.getBody().request()).isNotNull();
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
        RemittanceRequestPaginationResponse emptyResponse = RemittanceRequestPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(0L)
                .request(mockRemittanceRequest)
                .list(List.of())
                .build();

        when(remittanceOperationsFacade.getOperationListResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyResponse);

        ResponseEntity<RemittanceRequestPaginationResponse> response =
                controller.getOperationList("REM000000101000", 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().list()).isEmpty();
        assertThat(response.getBody().total()).isZero();
        assertThat(response.getBody().request()).isNotNull();
    }
}