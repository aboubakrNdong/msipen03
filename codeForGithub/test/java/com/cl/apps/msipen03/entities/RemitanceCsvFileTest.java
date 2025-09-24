package com.cl.apps.msipen03.entities;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


class RemitanceCsvFileTest {


    @Test
    void testShouldCreateRecordAndExposeFields() {

        byte[] data = "csv-bytes".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        RemitanceCsvFile file = new RemitanceCsvFile(in, 1234L, "");

        assertThat(file).isNotNull();
        assertThat(file.idReper()).isEqualTo(1234);
        assertThat(file.contentOfCsvFile()).isNotNull();

        byte[] readData = file. contentOfCsvFile().readAllBytes();
        assertThat(readData).isEqualTo(data);

    }
}