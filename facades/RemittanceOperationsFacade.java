package com.cl.msipen03.facades;

import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.UUID;
import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.entities.RemittanceOperationsPaginationResponse;
import com.cl.msipen03.service.RemittanceOperationsStatusService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RemittanceOperationsFacade {

    private final RemittanceOperationsStatusService remittanceOperationsStatusService;

    public RemittanceOperationsPaginationResponse getOperationListResponse(String remittanceOrderId, int page, int size) {
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString());

        Page<RemittanceOperationRequest> remittanceOperations = remittanceOperationsStatusService
                .getRemittanceOperationResponse(remittanceOrderId, page, size);

        return RemittanceOperationsPaginationResponse.builder()
                .size(remittanceOperations.getSize())
                .page(remittanceOperations.getNumber())
                .total(remittanceOperations.getTotalElements())
                .list(remittanceOperations.getContent())
                .build();
    }
}