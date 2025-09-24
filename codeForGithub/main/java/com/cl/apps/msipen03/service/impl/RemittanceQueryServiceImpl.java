package com.cl.apps.msipen03.service.impl;


import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import com.cl.apps.msipen03.repository.RemittanceOperationRepository;
import com.cl.apps.msipen03.repository.RemittanceOrderRepository;
import com.cl.apps.msipen03.service.RemittanceQueryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemittanceQueryServiceImpl implements RemittanceQueryService {

    private final RemittanceOrderRepository orderRepository;
    private final RemittanceOperationRepository operationRepository;


    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceCsvFileServiceImpl.class).logger();


    @Override
    @Transactional(readOnly = true)
    public Optional<RemittanceOrder> getRemittanceOrder(String referenceComete) throws DataAccessException {
        LOGGER.info("Retrieving remittance order {}", referenceComete);
        Optional<RemittanceOrder> order = orderRepository.findByRereferenceComete(referenceComete);
        LOGGER.info("Found order: {}", order.isPresent());
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RemittanceOperation> getRemittanceOperations(String referenceComete, int page, int size) throws DataAccessException {
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Retrieving remittance order with operation {}", referenceComete);
        Optional<RemittanceOrder> order = orderRepository.findByRereferenceComete(referenceComete);
        return order.map(o -> operationRepository.findOperationsByReferenceComete(
                        referenceComete,
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable))
                .orElse(Page.empty());
    }
}
