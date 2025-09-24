package com.cl.apps.msipen03.facades;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.service.RemittanceQueryService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemittanceOperationFacadeTest {

    @Mock
    private RemittanceQueryService remittanceQueryService;

    @InjectMocks
    private RemittanceOperationsFacade facade;

    private RemittanceOrder mockRequest;
    private List<RemittanceOperation> mockOperations;

    @BeforeEach
    void setUp() {
        MDC.clear();
        mockRequest = createMockRemittanceOrder();
        mockOperations = createMockOperations();
    }

    private RemittanceOrder createMockRemittanceOrder() {
        RemittanceOrder order = new RemittanceOrder();
        order.setReferenceBanqueDeLaRemise("IPF000000127644");
        order.setReferenceBanqueDeLaRemise("REM000000101000");
        order.setStatutDeLaRemise("PART");
        order.setLibelleDuCompteDO("CONDAT SAS");
        order.setCompteDO("FR8230002002600000000013S10");
        order.setNombreVirements(11);
        order.setDateExecution(Date.valueOf("2025-07-28"));
        order.setMontantTotalDeLaRemise(new BigDecimal("100057.00"));
        return order;
    }

    private List<RemittanceOperation> createMockOperations() {
        RemittanceOperation operation1 = createOperation("IPF000000127611");
        RemittanceOperation operation2 = createOperation("IPF000000127612");
        return Arrays.asList(operation1, operation2);
    }

    private RemittanceOperation createOperation(String referenceBanque) {
        RemittanceOperation operation = new RemittanceOperation();
        operation.setNomBeneficiaire("LCL CREDIT LYONNAIS");
        operation.setIbanBeneficiaire("FR8230002003870000017010T61");
        operation.setBicBeneficare("CRLYFRPPXXX");
        operation.setMontant(new BigDecimal("500.00"));
        operation.setDevise("EUR");
        operation.setMotifPaiement("0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA");
        operation.setReferencePaiement("PACIFICA2000157390");
        operation.setReferenceBoutEnBout("0157390153517262");
        operation.setReferenceBanque(referenceBanque);
        operation.setStatut("REJECTEE");
        operation.setCodeRejetIso("AC03");
        operation.setMotifRejet("Specific transaction/message amount is greater than allowed maximum");
        return operation;
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void testShouldReturnPaginatedResponse() {
        Page<RemittanceOperation> page = new PageImpl<>(mockOperations, PageRequest.of(0, 10), 11);
        when(remittanceQueryService.getRemittanceOperations(anyString(), anyInt(), anyInt()))
                .thenReturn(page);
        when(remittanceQueryService.getRemittanceOrder(anyString()))
                .thenReturn(Optional.of(mockRequest));

        RemittanceOrderPaginationResponse response = facade.getOperationListResponse(
                "2025-03-21 17:38:29.339830", 0, 10);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isZero();
        assertThat(response.total()).isEqualTo(11);
        assertThat(response.list()).hasSize(2);
        assertThat(response.request()).isNotNull();
        assertThat(response.request().getReferenceBanqueDeLaRemise()).isEqualTo("REM000000101000");
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }

    @Test
    void testShouldHandleEmptyResponse() {
        Page<RemittanceOperation> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10),
                0
        );

        when(remittanceQueryService.getRemittanceOperations(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyPage);
        when(remittanceQueryService.getRemittanceOrder(anyString()))
                .thenReturn(Optional.of(mockRequest));

        RemittanceOrderPaginationResponse response = facade.getOperationListResponse(
                "2025-03-21 17:38:29.339830", 0, 10);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isZero();
        assertThat(response.total()).isZero();
        assertThat(response.list()).isEmpty();
        assertThat(response.request()).isNotNull();
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }

    @Test
    void testShouldHandlePaginationParameters() {
        Page<RemittanceOperation> page = new PageImpl<>(mockOperations, PageRequest.of(1, 2), 11);

        when(remittanceQueryService.getRemittanceOperations(anyString(), anyInt(), anyInt()))
                .thenReturn(page);
        when(remittanceQueryService.getRemittanceOrder(anyString()))
                .thenReturn(Optional.of(mockRequest));

        RemittanceOrderPaginationResponse response = facade.getOperationListResponse(
                "2025-03-21 17:38:29.339830", 1, 2);

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.total()).isEqualTo(11);
        assertThat(response.list()).hasSize(2);
        assertThat(response.request()).isNotNull();
        assertThat(MDC.get("CORRELATION_ID")).isNotNull();
    }
}