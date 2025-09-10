package com.cl.apps.msipen03.facades;

import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.apps.msipen03.entities.RemitanceCsvFile;
import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.service.RemittanceCsvFileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RemittanceCsvFileFacade {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceCsvFileFacade.class).logger();
    private final RemittanceOperationsFacade remittanceOperationsFacade;
    private final RemittanceCsvFileService remittanceCsvFileService;

    public RemitanceCsvFile generateCsvFile(String remittanceOrderId) {
        LOGGER.info("Preparing Csv file for remittanceOrderId: {}", remittanceOrderId);

        RemittanceOrderPaginationResponse response = remittanceOperationsFacade
                .getOperationListResponse(remittanceOrderId, 0, Integer.MAX_VALUE);

        Long idReper = response.request().getIdReper();
        LOGGER.info("Processing export for id_reper: {}", idReper);

        ByteArrayInputStream contentOfCsvFile = remittanceCsvFileService.generateCsvFileContent(response);

        generateFilename(remittanceOrderId);

        String outputPathFile = "";

        return new RemitanceCsvFile(contentOfCsvFile, idReper, outputPathFile);

    }


    public String generateFilename(String remittanceOrderId) {
        return String.format("Remise_%s_%s.csv", remittanceOrderId, LocalDate.now());
    }

}