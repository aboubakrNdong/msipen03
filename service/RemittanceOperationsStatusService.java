package com.cl.msipen03.service;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.repository.DatabaseRepository;

@Component
public class RemittanceOperationsStatusService {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceOperationsStatusService.class).logger();

    @Autowired
    private DatabaseRepository databaseRepository;

    public Page<RemittanceOperationRequest> getRemittanceOperationResponse(String remittanceOrderId, int page, int size) {
        LOGGER.info("Start - RemittanceOperationService: getRemittanceOperationResponse: ");
        return databaseRepository.listRemittanceOperation(remittanceOrderId, page, size);
    }

}
