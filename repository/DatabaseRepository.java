package com.cl.msipen03.repository;

import javax.sql.DataSource;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.mapper.RemittanceOperationsListMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.service.RemittanceOperationsStatusService;

import java.util.List;

@Repository
@Transactional
public class DatabaseRepository {

    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceOperationsStatusService.class).logger();

    public DatabaseRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Page<RemittanceOperationRequest> listRemittanceOperation(String remittanceOrderId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        String sql = "select * from virinst1a.fct_feedback_masse_restitution_statut_operation_remise(?,?,?)";

        LOGGER.info("Execute query - RemittanceDatabaseRespository: fct_feedback_masse_restitution_statut_operation_remise:");

        List<RemittanceOperationRequest> remittanceOperationRequestList = jdbcTemplate.query(sql, new RemittanceOperationsListMapper(),
                remittanceOrderId, page, size);
        long total = (!remittanceOperationRequestList.isEmpty() ? remittanceOperationRequestList.get(0).totalCount() : 0);

        LOGGER.info("Execute query - fct_Remittance_total_operation: total: {} ", total);

        return new PageImpl<>(remittanceOperationRequestList, pageable, total);
    }

}
