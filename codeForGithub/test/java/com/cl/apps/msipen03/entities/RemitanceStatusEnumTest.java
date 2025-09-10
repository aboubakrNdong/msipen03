package com.cl.apps.msipen03.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class RemitanceStatusEnumTest {

    @Test
    void testShouldexposeLabelsForEachEnum() {

        assertThat(RemittanceStatusEnum.ACCP.getLabel()).isEqualTo("ACCEPTEE");
        assertThat(RemittanceStatusEnum.RJCT.getLabel()).isEqualTo("REJETEE");
        assertThat(RemittanceStatusEnum.PART.getLabel()).isEqualTo("PARTIELLEMENT ACCEPTEE");

    }

    @Test
    void testGetStatusLabelShouldReturnInputForUnknownOrNullCodes() {

        assertThat(RemittanceStatusEnum.getStatusLabel("UNKNOWN")).isEqualTo("UNKNOWN");
        assertThat(RemittanceStatusEnum.getStatusLabel(null)).isNull();

    }
}