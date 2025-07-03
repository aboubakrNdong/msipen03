package com.cl.msipen03.facades;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.entities.RemittanceOperationsPaginationResponse;
import com.cl.msipen03.service.RemittanceOperationsStatusService;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemittanceOperationsFacadeTest {

    @Mock
    private RemittanceOperationsStatusService remittanceOperationsStatusService;

    @InjectMocks
    private RemittanceOperationsFacade facade;

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void testShouldReturnPaginatedResponse() {
        List<RemittanceOperationRequest> operations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 2L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 2L)
        );
        Page<RemittanceOperationRequest> page = new PageImpl<>(operations, PageRequest.of(0, 10), 2);

        when(remittanceOperationsStatusService.getRemittanceOperationResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(page);

        RemittanceOperationsPaginationResponse response = facade.getOperationListResponse(
                "IPF000000127670", 0, 10);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isZero();
        assertThat(response.total()).isEqualTo(2);
        assertThat(response.list()).hasSize(2);
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }

    @Test
    void testShouldHandleEmptyResponse() {
        Page<RemittanceOperationRequest> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10),
                0
        );

        when(remittanceOperationsStatusService.getRemittanceOperationResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyPage);

        RemittanceOperationsPaginationResponse response = facade.getOperationListResponse(
                "IPF000000127670", 0, 10);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isZero();
        assertThat(response.total()).isZero();
        assertThat(response.list()).isEmpty();
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }

    @Test
    void testShouldHandlePaginationParameters() {
        List<RemittanceOperationRequest> operations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 5L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 5L)
        );
        Page<RemittanceOperationRequest> page = new PageImpl<>(operations, PageRequest.of(1, 2), 5);

        when(remittanceOperationsStatusService.getRemittanceOperationResponse(anyString(), anyInt(), anyInt()))
                .thenReturn(page);

        RemittanceOperationsPaginationResponse response = facade.getOperationListResponse(
                "IPF000000127670", 1, 2);

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.total()).isEqualTo(5);
        assertThat(response.list()).hasSize(2);
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }
}