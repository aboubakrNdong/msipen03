package com.cl.apps.msipen03.repository;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.exceptions.InternalMSIPEN03Exception;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import com.cl.apps.msipen03.entities.*;

@ExtendWith(MockitoExtension.class)
class RemittanceOperationRepositoryTest {

    @Mock
    private RemittanceOperationRepository repository;


    @Test
    void testShouldFindOperationByRemittanceOrderId_withPagination() throws InternalMSIPEN03Exception {

        String orderId = "REM000000101000";
        Pageable pageable = PageRequest.of(0, 10);

        List<RemittanceOperation> content = List.of(buildOperation("IPF000000127611"),
                buildOperation("IPF0000001276694"));
        Page<RemittanceOperation> page = new PageImpl<>(content, pageable, 12);

        when(repository.findOperationsByRemittanceOrderId(eq(orderId), eq(pageable.getPageNumber()), eq(pageable.getPageSize()), eq(pageable)))
                .thenReturn(page);

        Page<RemittanceOperation> result = repository.findOperationsByRemittanceOrderId(orderId, pageable.getPageNumber(), pageable.getPageSize(), pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(12);

    }

    @Test
    void shouldReturnEmptyPage_whenNoOperations() {

        String orderId = "REM000000101999";
        Pageable pageable = PageRequest.of(0, 10);
        Page<RemittanceOperation> empty = Page.empty(pageable);

        when(repository.findOperationsByRemittanceOrderId(anyString(), anyInt(), anyInt(), any(Pageable.class)))
                .thenReturn(empty);

        Page<RemittanceOperation> result = repository.findOperationsByRemittanceOrderId(orderId, pageable.getPageNumber(), pageable.getPageSize(), pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getSize()).isEqualTo(10);

    }

    private RemittanceOperation buildOperation(String referenceBanque) {
        RemittanceOperation op = new RemittanceOperation();
        op.setNomBeneficiaire("LCL CREDIT LYONNAIS");
        op.setIbanBeneficiaire("30002095050000079135P57");
        op.setBicBeneficare("CRLYFRPPXXX");
        op.setMontant(new BigDecimal("500.00"));
        op.setDevise("EUR");
        op.setMotifPaiement("0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA");
        op.setReferencePaiement("PACIFICA2000157390");
        op.setReferenceBoutEnBout("0157390153517262");
        op.setReferenceBanque(referenceBanque);
        op.setStatut("REJECTED");
        op.setCodeRejetIso("AC03");
        op.setMotifRejet("Account number is invalid or missing.");
        return op;
    }

}