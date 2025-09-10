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
    public Optional<RemittanceOrder> getRemittanceOrder(String orderId) throws DataAccessException {
        LOGGER.info("Retrieving remittance order {}", orderId);
        Optional<RemittanceOrder> order = orderRepository.findByRemittanceOrderId(orderId);
        LOGGER.info("Found order: {}", order.isPresent());
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RemittanceOperation> getRemittanceOperations(String orderId, int page, int size) throws DataAccessException {
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Retrieving remittance order with operation {}", orderId);
        Optional<RemittanceOrder> order = orderRepository.findByRemittanceOrderId(orderId);
        return order.map(o -> operationRepository.findOperationsByRemittanceOrderId(
                        orderId,
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable))
                .orElse(Page.empty());
    }
}
