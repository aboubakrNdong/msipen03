package com.cl.msipen03.facades;

import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.entities.ExcelGenerationEntity;
import com.cl.msipen03.entities.RemittanceRequestPaginationResponse;
import com.cl.msipen03.service.RemittanceBuildExcelService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RemittanceBuildExcelFacade {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceBuildExcelFacade.class).logger();
    private final RemittanceOperationsFacade remittanceOperationsFacade;
    private final RemittanceBuildExcelService remittanceBuildExcelService;

    public ExcelGenerationEntity generateExcelFile(String remittanceOrderId) {
        LOGGER.info("Preparing Excel file for remittanceOrderId: {}", remittanceOrderId);

        RemittanceRequestPaginationResponse response = remittanceOperationsFacade
                .getOperationListResponse(remittanceOrderId, 0, Integer.MAX_VALUE);

        Integer idReper = response.request().id_reper();
        LOGGER.info("Processing export for id_reper: {}", idReper);

        ByteArrayInputStream contentOfExcelFile = remittanceBuildExcelService.generateExcelContent(response);

        generateFilename(remittanceOrderId);

        return new ExcelGenerationEntity(contentOfExcelFile, idReper);
    }

    public String generateFilename(String remittanceOrderId) {
        return String.format("Remise_%s_%s.xlsx", remittanceOrderId, Timestamp.from(Instant.now()));
    }
}