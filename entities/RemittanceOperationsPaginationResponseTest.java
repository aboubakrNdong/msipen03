package com.cl.msipen03.entities;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RemittanceOperationsPaginationResponseTest {

    @Test
    void testShouldCreatePaginationResponse() {
        List<RemittanceOperationRequest> operations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 2L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 2L)
        );

        RemittanceOperationsPaginationResponse response = RemittanceOperationsPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(2L)
                .list(operations)
                .build();

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.page()).isEqualTo(0);
        assertThat(response.total()).isEqualTo(2L);
        assertThat(response.list()).hasSize(2);
    }

    @Test
    void testShouldSerializeToJson() throws Exception {
        List<RemittanceOperationRequest> operations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 2L)
        );

        RemittanceOperationsPaginationResponse response = RemittanceOperationsPaginationResponse.builder()
                .size(10)
                .page(0)
                .total(1L)
                .list(operations)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);

        assertThat(json).contains("\"size\":10");
        assertThat(json).contains("\"page\":0");
        assertThat(json).contains("\"total\":1");
        assertThat(json).contains("\"Remittance_operations\":");
    }
}