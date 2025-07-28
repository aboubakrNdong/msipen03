package com.cl.msipen03.service;

import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.repository.DatabaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class RemittanceOperationsStatusServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @InjectMocks
    private RemittanceStatusService service;

    private List<RemittanceOperations> mockOperations;
    private RemittanceRequest mockRequest;
    private Page<RemittanceOperations> mockPage;

    @BeforeEach
    void setUp() {
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
                        "PACIFICA2000157390",
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
                        "PACIFICA2000157390",
                        new BigDecimal("500.00"),
                        "EUR",
                        "BNK124",
                        "REJECTED",
                        "Account number is invalid or missing.",
                        2L
                )
        );

        mockPage = new PageImpl<>(mockOperations, PageRequest.of(0, 10), 2);
    }

    @Test
    void testShouldReturnRemittanceOperations() {
        when(databaseRepository.findByRemittanceOrderIdPagination(anyString(), anyInt(), anyInt()))
                .thenReturn(mockPage);

        Page<RemittanceOperations> result = service.getRemittanceOperationResponse("REM000000101000", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2L);
        assertThat(result.getContent().get(0).prenom_nom_beneficiaire()).isEqualTo("LCL CREDIT LYONNAIS");
        assertThat(result.getContent().get(0).bic_beneficiare()).isEqualTo("CRLYFRPPXXX");
        assertThat(result.getContent().get(0).montant_virement()).isEqualTo(new BigDecimal("500.00"));
        assertThat(result.getContent().get(0).reference_Bout_en_bout()).isEqualTo("PACIFICA2000157390");
    }

    @Test
    void testShouldReturnRemittanceRequest() {
        when(databaseRepository.findByRemittanceOrderId(anyString()))
                .thenReturn(mockRequest);

        RemittanceRequest result = service.getRemittanceRequestResponse("REM000000101000");

        assertThat(result).isNotNull();
        assertThat(result.numero_de_la_remise()).isEqualTo("REM000000101000");
        assertThat(result.statut_de_la_remise()).isEqualTo("PART");
        assertThat(result.libelle_du_compte_DO()).isEqualTo("CONDAT SAS");
        assertThat(result.montant_total_de_la_remise()).isEqualTo(new BigDecimal("100057.00"));
    }

    @Test
    void testShouldReturnEmptyPage() {
        Page<RemittanceOperations> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(databaseRepository.findByRemittanceOrderIdPagination(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyPage);

        Page<RemittanceOperations> result = service.getRemittanceOperationResponse("REM000000101000", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }
}