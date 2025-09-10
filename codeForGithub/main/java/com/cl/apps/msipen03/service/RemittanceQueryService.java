package com.cl.apps.msipen03.service;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RemittanceQueryService {

    Optional<RemittanceOrder> getRemittanceOrder(String remittanceOrderId);

    Page<RemittanceOperation> getRemittanceOperations(String remittanceOrderId, int page, int size);

}
