package com.cl.msipen03.service;

import com.cl.msipen03.entities.RemittanceRequestPaginationResponse;
import java.io.ByteArrayInputStream;

public interface RemittanceBuildExcelService {
    ByteArrayInputStream generateExcelContent(RemittanceRequestPaginationResponse response);
}