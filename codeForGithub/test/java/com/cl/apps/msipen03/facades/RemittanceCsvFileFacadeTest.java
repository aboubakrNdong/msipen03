package com.cl.apps.msipen03.facades;

import com.cl.apps.msipen03.entities.RemitanceCsvFile;
import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.service.RemittanceCsvFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemittanceCsvFileFacadeTest {

    @Mock
    private RemittanceOperationsFacade remittanceOperationsFacade;

    @Mock
    private RemittanceCsvFileService remittanceCsvFileService;

    @InjectMocks
    private RemittanceCsvFileFacade facade;

    private RemittanceOrderPaginationResponse mockResponse;
    private RemittanceOrder mockOrder;
    private List<RemittanceOperation> mockOperations;
    private String referenceComete;

    @BeforeEach
    void setUp() {
        referenceComete = "2025-03-21 17:38:29.339830";
        mockOrder = createMockOrder();
        mockOperations = List.of(createMockOperation());
        mockResponse = createMockResponse();
    }

    private RemittanceOrder createMockOrder() {
        RemittanceOrder order = new RemittanceOrder();
        order.setIdReper(1234L);
        order.setReferenceBanqueDeLaRemise(referenceComete);
        order.setStatutDeLaRemise("PART");
        order.setLibelleDuCompteDO("CONDAT SAS");
        order.setCompteDO("FR8230002002600000000013S10");
        order.setNombreVirements(1);
        order.setDateExecution(Date.valueOf("2025-03-29"));
        order.setMontantTotalDeLaRemise(new BigDecimal("500.00"));
        return order;
    }

    private RemittanceOperation createMockOperation() {
        RemittanceOperation operation = new RemittanceOperation();
        operation.setNomBeneficiaire("LCL CREDIT LYONNAIS");
        operation.setIbanBeneficiaire("FR8230002003870000017010T61");
        operation.setBicBeneficare("CRLYFRPPXXX");
        operation.setMontant(new BigDecimal("500.00"));
        operation.setDevise("EUR");
        operation.setMotifPaiement("REMONTEE COTISATIONS");
        operation.setReferencePaiement("PACIFICA2000157390");
        operation.setReferenceBoutEnBout("0157390153517262");
        operation.setReferenceBanque("IPF000000127611");
        operation.setStatut("ACCEPTEE");
        operation.setCodeRejetIso("AC03");
        operation.setMotifRejet("Invalid account number");
        return operation;
    }

    private RemittanceOrderPaginationResponse createMockResponse() {
        return RemittanceOrderPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(1L)
                .request(mockOrder)
                .list(mockOperations)
                .build();
    }

    @Test
    void shouldGenerateCsvFile() {
        ByteArrayInputStream mockContent = new ByteArrayInputStream("Remise_".getBytes());

        when(remittanceOperationsFacade.getOperationListResponse(referenceComete, 0, Integer.MAX_VALUE))
                .thenReturn(mockResponse);
        when(remittanceCsvFileService.generateCsvFileContent(any()))
                .thenReturn(mockContent);

        RemitanceCsvFile result = facade.generateCsvFile(referenceComete);

        assertThat(result).isNotNull();
        assertThat(result.contentOfCsvFile()).isNotNull();
        assertThat(result.idReper()).isEqualTo(1234);
    }

    @Test
    void shouldGenerateFilename() {
        String expectedDate = LocalDate.now().toString();
        String result = facade.generateFilename(referenceComete);

        assertThat(result).isNotNull()
                .startsWith("Remise_")
                .contains(referenceComete)
                .contains(expectedDate)
                .endsWith(".csv");
    }
}