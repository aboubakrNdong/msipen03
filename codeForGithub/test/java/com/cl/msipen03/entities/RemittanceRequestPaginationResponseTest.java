package com.cl.msipen03.entities;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RemittanceRequestPaginationResponseTest {
    Date executionDate = Date.valueOf("2025-07-28");

    @Test
    void testShouldCreatePaginationResponse() {
        RemittanceRequest request = new RemittanceRequest(
                123456, // Added id_reper
                Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)),
                "REM000000101000",
                "PART",
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


        RemittanceRequestPaginationResponse response = RemittanceRequestPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(2L)
                .request(request)
                .list(operations)
                .build();

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isEqualTo(0);
        assertThat(response.total()).isEqualTo(2L);
        assertThat(response.list()).hasSize(2);
        assertThat(response.request()).isNotNull();
    }

    @Test
    void testShouldSerializeToJson() throws Exception {
        RemittanceRequest request = new RemittanceRequest(
                123456,
                Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)),
                "REM000000101000",
                "PART",
                "CONDAT SAS",
                102481,
                1,
                executionDate,
                new BigDecimal("500.00")
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
                )
        );

        RemittanceRequestPaginationResponse response = RemittanceRequestPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(1L)
                .request(request)
                .list(operations)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);

        assertThat(json).contains("\"size\":10");
        assertThat(json).contains("\"page\":0");
        assertThat(json).contains("\"total\":1");
        assertThat(json).contains("\"Synthese_Operations\":");
        assertThat(json).contains("\"Mes_Virements\":");
    }
}