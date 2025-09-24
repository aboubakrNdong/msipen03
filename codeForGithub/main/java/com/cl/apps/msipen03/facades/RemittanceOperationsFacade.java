package com.cl.apps.msipen03.facades;

import com.cl.apps.msipen03.entities.RemittanceOrder;
import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.exceptions.RemittanceNotFoundException;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.service.RemittanceQueryService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RemittanceOperationsFacade {

    private final RemittanceQueryService remittanceQueryService;

    public RemittanceOrderPaginationResponse getOperationListResponse(String referenceComete, int page, int size) {
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString());

        Page<RemittanceOperation> remittanceOperations = remittanceQueryService
                .getRemittanceOperations(referenceComete, page, size);

        RemittanceOrder remittanceOrder = remittanceQueryService.getRemittanceOrder(referenceComete)
                .orElseThrow(() -> new RemittanceNotFoundException(" order not found : " + referenceComete));

        return RemittanceOrderPaginationResponse.builder()
                .size(remittanceOperations.getSize())
                .page(remittanceOperations.getNumber())
                .total(remittanceOperations.getTotalElements())
                .request(remittanceOrder)
                .list(remittanceOperations.getContent())
                .build();
    }
}