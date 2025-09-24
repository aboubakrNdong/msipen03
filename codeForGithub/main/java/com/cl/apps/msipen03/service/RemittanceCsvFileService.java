package com.cl.apps.msipen03.service;

import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;

import java.io.ByteArrayInputStream;

public interface RemittanceCsvFileService {
    ByteArrayInputStream generateCsvFileContent(RemittanceOrderPaginationResponse response);
}