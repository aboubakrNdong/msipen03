package com.cl.msipen03.facades;

import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.entities.RemittanceRequestPaginationResponse;
import com.cl.msipen03.service.RemittanceStatusService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemittanceOperationsFacadeTest {

    @Mock
    private RemittanceStatusService remittanceStatusService;

    @InjectMocks
    private RemittanceOperationsFacade facade;

    private RemittanceRequest mockRequest;
    private List<RemittanceOperations> mockOperations;

    @BeforeEach
    void setUp() {

        MDC.clear();
        Date executionDate = Date.valueOf("2025-07-28");
        LocalDateTime creationDate = LocalDateTime.of(2024, 6, 19, 14, 3, 41);

        mockRequest = new RemittanceRequest(
                123456,
                Timestamp.from(creationDate.toInstant(ZoneOffset.UTC)),
                "REM000000101000",
                "PART",
                "CONDAT SAS",
                102481,
                11,
                executionDate,
                new BigDecimal("100057.00")
        );

        mockOperations = Arrays.asList(
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
                )
        );
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void testShouldReturnPaginatedResponse() {
        Page<RemittanceOperations> page = new PageImpl<>(mockOperations, PageRequest.of(0, 10), 11);
        when(remittanceStatusService.getRemittanceOperationResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(page);
        when(remittanceStatusService.getRemittanceRequestResponse(anyString()))
                .thenReturn(mockRequest);

        RemittanceRequestPaginationResponse response = facade.getOperationListResponse(
                "REM000000101000", 0, 10);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isZero();
        assertThat(response.total()).isEqualTo(11);
        assertThat(response.list()).hasSize(2);
        assertThat(response.request()).isNotNull();
        assertThat(response.request().numero_de_la_remise()).isEqualTo("REM000000101000");
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }

    @Test
    void testShouldHandleEmptyResponse() {
        Page<RemittanceOperations> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10),
                0
        );

        when(remittanceStatusService.getRemittanceOperationResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyPage);
        when(remittanceStatusService.getRemittanceRequestResponse(anyString()))
                .thenReturn(mockRequest);

        RemittanceRequestPaginationResponse response = facade.getOperationListResponse(
                "REM000000101000", 0, 10);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isZero();
        assertThat(response.total()).isZero();
        assertThat(response.list()).isEmpty();
        assertThat(response.request()).isNotNull();
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }

    @Test
    void testShouldHandlePaginationParameters() {
        Page<RemittanceOperations> page = new PageImpl<>(mockOperations, PageRequest.of(1, 2), 11);

        when(remittanceStatusService.getRemittanceOperationResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(page);
        when(remittanceStatusService.getRemittanceRequestResponse(anyString()))
                .thenReturn(mockRequest);

        RemittanceRequestPaginationResponse response = facade.getOperationListResponse(
                "REM000000101000", 1, 2);

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.total()).isEqualTo(11);
        assertThat(response.list()).hasSize(2);
        assertThat(response.request()).isNotNull();
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }
}