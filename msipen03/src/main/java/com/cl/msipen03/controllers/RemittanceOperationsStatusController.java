package com.cl.msipen03.controllers;

import java.util.UUID;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.entities.RemittanceOperationsPaginationResponse;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.exceptions.InternalMSIPEN03Exception;
import com.cl.msipen03.service.RemittanceOperationsStatusService;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/msipen03")
public class RemittanceOperationsStatusController {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceOperationsStatusController.class).logger();

    private final RemittanceOperationsStatusService remittanceOperationsStatusService;

    @GetMapping(value = "remittance/operations/{remittanceOrderId}")
    public ResponseEntity<RemittanceOperationsPaginationResponse> getOperationList(
            @PathVariable String remittanceOrderId,
            @RequestParam int page, @RequestParam int size)
            throws InternalMSIPEN03Exception {

        // initialisation du CORRELATION ID
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString());
        Page<RemittanceOperationRequest> RemittanceOperations = remittanceOperationsStatusService.getRemittanceOperationResponse(remittanceOrderId, page,
                size);

        RemittanceOperationsPaginationResponse remittanceOperationsListResponse = RemittanceOperationsPaginationResponse.builder()
                .size(RemittanceOperations.getSize()).page(RemittanceOperations.getNumber())
                .total(RemittanceOperations.getTotalElements()).list(RemittanceOperations.getContent()).build();

        return new ResponseEntity<>(remittanceOperationsListResponse, HttpStatus.OK);
    }
}