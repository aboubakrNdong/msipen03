package com.cl.msipen03.facades;

import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.entities.RemittanceRequestPaginationResponse;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.UUID;
import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.service.RemittanceStatusService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RemittanceOperationsFacade {

    private final RemittanceStatusService remittanceStatusService;

    public RemittanceRequestPaginationResponse getOperationListResponse(String remittanceOrderId, int page, int size) {
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString());

        Page<RemittanceOperations> remittanceOperations = remittanceStatusService
                .getRemittanceOperationResponse(remittanceOrderId, page, size);

        RemittanceRequest remittanceRequest = remittanceStatusService.getRemittanceRequestResponse(remittanceOrderId);


        return RemittanceRequestPaginationResponse.builder()
                .size(remittanceOperations.getSize())
                .page(remittanceOperations.getNumber())
                .total(remittanceOperations.getTotalElements())
                .request(remittanceRequest)
                .list(remittanceOperations.getContent())
                .build();
    }
}