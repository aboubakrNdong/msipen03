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



    @GetMapping(value = "remittance/operations/{referenceComete}")
    public ResponseEntity<RemittanceOrderPaginationResponse> getOperationList(
            @Parameter(description = "Reference COMETE", example = "REM000000101247") //TODO: Replace with reference from COMETE when
            @PathVariable String referenceComete,   // TODO: when the stock proc robot ROBVMAMQ is finalized and there is data in the database,
            @RequestParam(defaultValue = "0") int page, //TODO: then you will have to finalize the stock proc to take the comete reference as input
            @RequestParam(defaultValue = "10") int size) throws InternalMSIPEN03Exception {

        LOGGER.info("Remittance Operations Status Controller:{}", referenceComete);

        RemittanceOrderPaginationResponse response = remittanceOperationsFacade
                .getOperationListResponse(referenceComete, page, size);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping(value = "remittance/operations/{referenceComete}/csv")
    public ResponseEntity<InputStreamResource> downloadCsvFile(
            @Parameter(description = "Reference COMETE", example = "REM000000101247")
            @PathVariable String referenceComete) {

        LOGGER.info("Generating CSV file for Reference COMETE: {}", referenceComete);
        RemitanceCsvFile csvFile = remittanceCsvFileFacade.generateCsvFile(referenceComete);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                remittanceCsvFileFacade.generateFilename(referenceComete));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csvFile.contentOfCsvFile()));
    }
}