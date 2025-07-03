package com.cl.msipen03.entities;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

class RemittanceOperationRequestTest {

    @Test
    void testShouldCreateRemittanceOperationRequest() {
        RemittanceOperationRequest request = new RemittanceOperationRequest(
                "IPF000000127670",
                "PART",
                10L
        );

        assertThat(request.OperationId()).isEqualTo("IPF000000127670");
        assertThat(request.OperationStatus()).isEqualTo("PART");
        assertThat(request.totalCount()).isEqualTo(10L);
    }

    @Test
    void testShouldSerializeToJson() throws Exception {
        RemittanceOperationRequest request = new RemittanceOperationRequest(
                "IPF000000127670",
                "PART",
                10L
        );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        assertThat(json).contains("\"Operation_id\":\"IPF000000127670\"");
        assertThat(json).contains("\"operation_status\":\"PART\"");
        assertThat(json).doesNotContain("totalCount");
    }
}