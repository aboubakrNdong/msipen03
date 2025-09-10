package com.cl.apps.msipen03.controllers;

import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.facades.RemittanceOperationsFacade;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.apps.msipen03.exceptions.InternalMSIPEN03Exception;
import com.cl.apps.msipen03.entities.RemitanceCsvFile;
import com.cl.apps.msipen03.facades.RemittanceCsvFileFacade;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/msipen03")
public class RemittanceOperationsStatusController {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceOperationsStatusController.class).logger();

    private final RemittanceOperationsFacade remittanceOperationsFacade;

    private final RemittanceCsvFileFacade remittanceCsvFileFacade;



    @GetMapping(value = "remittance/operations/{remittanceOrderId}")
    public ResponseEntity<RemittanceOrderPaginationResponse> getOperationList(
            @Parameter(description = "Remittance order identifier)", example = "REM000000101247")
            @PathVariable String remittanceOrderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws InternalMSIPEN03Exception {

        LOGGER.info("Remittance Operations Status Controller:{}", remittanceOrderId);

        RemittanceOrderPaginationResponse response = remittanceOperationsFacade
                .getOperationListResponse(remittanceOrderId, page, size);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping(value = "remittance/operations/{remittanceOrderId}/csv")
    public ResponseEntity<InputStreamResource> downloadCsvFile(
            @Parameter(description = "Remittance order identifier", example = "REM000000101247")
            @PathVariable String remittanceOrderId) {

        LOGGER.info("Generating CSV file for remittanceOrderId: {}", remittanceOrderId);
        RemitanceCsvFile csvFile = remittanceCsvFileFacade.generateCsvFile(remittanceOrderId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                remittanceCsvFileFacade.generateFilename(remittanceOrderId));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csvFile.contentOfCsvFile()));
    }
}