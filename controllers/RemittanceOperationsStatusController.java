package com.cl.msipen03.controllers;

import com.cl.msipen03.entities.RemittanceOperationsPaginationResponse;
import com.cl.msipen03.facades.RemittanceOperationsFacade;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.exceptions.InternalMSIPEN03Exception;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/msipen03")
public class RemittanceOperationsStatusController {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceOperationsStatusController.class).logger();

    private final RemittanceOperationsFacade remittanceOperationsFacade;

    @GetMapping(value = "remittance/operations/{remittanceOrderId}")
    public ResponseEntity<RemittanceOperationsPaginationResponse> getOperationList(
            @PathVariable String remittanceOrderId,
            @RequestParam int page,
            @RequestParam int size) throws InternalMSIPEN03Exception {

        LOGGER.info("Remittance Operations Status Controller:{}", remittanceOrderId);

        RemittanceOperationsPaginationResponse response = remittanceOperationsFacade
                .getOperationListResponse(remittanceOrderId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}