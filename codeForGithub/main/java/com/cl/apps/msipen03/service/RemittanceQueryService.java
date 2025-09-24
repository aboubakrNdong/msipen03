package com.cl.apps.msipen03.service;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RemittanceQueryService {

    Optional<RemittanceOrder> getRemittanceOrder(String referenceComete);

    Page<RemittanceOperation> getRemittanceOperations(String referenceComete, int page, int size);

}
