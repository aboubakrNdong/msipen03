package com.cl.msipen03.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

class RemittanceOperationsTest {

    private final Date executionDate = Date.valueOf("2025-07-28");

    private RemittanceOperations createOp(String refBout, long count) {
        return new RemittanceOperations(
                "LCL CREDIT LYONNAIS",
                "30002003870000017010T61",
                "CRLYFRPPXXX",
                executionDate,
                "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                "PACIFICA2000157390",
                refBout,
                new BigDecimal("500.00"),
                "EUR",
                "BNK123",
                "REJECTED",
                "Specific transaction/message amount is greater than allowed maximum",
                count
        );
    }

    @Test
    void testShouldCreateRemittanceOperations() {
        RemittanceOperations op = createOp("REF123", 2L);

        assertThat(op.prenom_nom_beneficiaire()).isEqualTo("LCL CREDIT LYONNAIS");
        assertThat(op.iban_beneficiaire()).isEqualTo("30002003870000017010T61");
        assertThat(op.bic_beneficiare()).isEqualTo("CRLYFRPPXXX");
        assertThat(op.date_execution_virement()).isEqualTo(executionDate);
        assertThat(op.motif_virement()).startsWith("0157390000153517262");
        assertThat(op.reference_virement()).isEqualTo("PACIFICA2000157390");
        assertThat(op.reference_Bout_en_bout()).isEqualTo("REF123");
        assertThat(op.montant_virement()).isEqualByComparingTo("500.00");
        assertThat(op.devise()).isEqualTo("EUR");
        assertThat(op.reference_banque_Operation()).isEqualTo("BNK123");
        assertThat(op.statut_virement()).isEqualTo("REJECTED");
        assertThat(op.motif_rejet()).contains("greater than allowed");
        assertThat(op.totalCount()).isEqualTo(2L);
    }

    @Test
    void testShouldSerializeToJson() throws Exception {
        RemittanceOperations op = createOp("REF124", 2L);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(op);

        assertThat(json).contains("\"reference_virement\":\"PACIFICA2000157390\"");
        assertThat(json).doesNotContain("totalCount");
    }
}
