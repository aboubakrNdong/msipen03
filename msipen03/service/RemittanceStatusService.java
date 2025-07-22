package com.cl.msipen03.service;

import com.cl.msipen03.entities.RemittanceOperation;
import com.cl.msipen03.entities.RemittanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import com.cl.msipen03.repository.DatabaseRepository;

@Component
@RequiredArgsConstructor
public class RemittanceStatusService {

    private final DatabaseRepository databaseRepository;

    public RemittanceRequest getRemittanceRequestResponse(String remittanceOrderId) {
        return databaseRepository.findByRemittanceOrderId(remittanceOrderId);
    }

    public Page<RemittanceOperation> getRemittanceOperationResponse(String remittanceOrderId, int page, int size) {
        return databaseRepository.findByRemittanceOrderIdPagination(remittanceOrderId, page, size);
    }


}
