package com.cl.apps.msipen03.service;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.service.impl.RemittanceCsvFileServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RemittanceCsvFileServiceImplTest {

    private final RemittanceCsvFileServiceImpl service = new RemittanceCsvFileServiceImpl();
    private final Date executionDate = Date.valueOf("2025-07-29");

    private RemittanceOrder buildOrder() {
        RemittanceOrder order = new RemittanceOrder();
        order.setReferenceBanqueDeLaRemise("IPF000000127611");
        order.setReferenceBanqueDeLaRemise("REM000000101000");
        order.setStatutDeLaRemise("PARTIAL");
        order.setLibelleDuCompteDO("CONDAT SAS");
        order.setCompteDO("FR7630006000011234567890189");
        order.setNombreVirements(2);
        order.setDateExecution(executionDate);
        order.setMontantTotalDeLaRemise(new BigDecimal("1234.50"));
        return order;
    }

    private RemittanceOperation buildOperation(String suffix) {
        RemittanceOperation operation = new RemittanceOperation();
        operation.setNomBeneficiaire("LCL CREDIT LYONNAIS" + suffix);
        operation.setIbanBeneficiaire("FR8230002003870000017010T61" + suffix);
        operation.setBicBeneficare("CRLYFRPPXXX" + suffix);
        operation.setMontant(new BigDecimal("500.00"));
        operation.setDevise("EUR");
        operation.setMotifPaiement("REMONTEE " + suffix);
        operation.setReferencePaiement("PACIFICA2000157390" + suffix);
        operation.setReferenceBoutEnBout("0157390153517262" + suffix);
        operation.setReferenceBanque("IPF000000127669" + suffix);
        operation.setStatut("ACCEPTEE");
        operation.setCodeRejetIso("AC03");
        operation.setMotifRejet("Creditor account number invalid or missing");
        return operation;
    }

    @Test
    void shouldGenerateCsv_withSyntheseAndOperations() throws Exception {
        var mockResponse = mock(RemittanceOrderPaginationResponse.class);
        RemittanceOrder order = buildOrder();
        List<RemittanceOperation> ops = List.of(buildOperation("A"), buildOperation("B"));

        when(mockResponse.request()).thenReturn(order);
        when(mockResponse.list()).thenReturn(ops);

        ByteArrayInputStream in = service.generateCsvFileContent(mockResponse);
        assertThat(in).isNotNull();

        // Read CSV content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            List<String> lines = reader.lines().toList();

            assertThat(lines.getFirst()).isEqualTo("COMPTE RENDU DE TRAITEMENT DE LA REMISE");

            assertThat(lines).anyMatch(line ->
                    line.startsWith("Reference Banque de la remise;REM000000101000"));
            assertThat(lines).anyMatch(line ->
                    line.startsWith("Statut de la remise;PARTIAL"));

            int detailIndex = lines.indexOf("DETAIL DES OPERATIONS DE LA REMISE");
            assertThat(detailIndex).isPositive();

            // Verify headers
            String[] expectedHeaders = {
                    "Nom beneficiaire", "Iban beneficiaire", "Bic beneficiaire ",
                    "Montant", "Devise", "Motif du paiement",
                    "Reference du paiement", "Reference de Bout en bout", "Reference Banque",
                    "Statut", "Code rejet ISO", "Motif de rejet"
            };
            assertThat(lines.get(detailIndex + 1)).contains(String.join(";", expectedHeaders));

            // Verify first operation
            String firstOpLine = lines.get(detailIndex + 2);
            assertThat(firstOpLine).contains("LCL CREDIT LYONNAISA");
            assertThat(firstOpLine).contains("30002003870000017010T61A");
            assertThat(firstOpLine).contains("CRLYFRPPXXXA");
        }
    }
}