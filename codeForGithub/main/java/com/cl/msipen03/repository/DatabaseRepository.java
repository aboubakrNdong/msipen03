package com.cl.msipen03.repository;

import javax.sql.DataSource;

import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.mapper.RemittanceOperationsMapper;
import com.cl.msipen03.mapper.RemittanceRequestMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.service.RemittanceStatusService;

import java.util.List;

@Repository
@Transactional
public class DatabaseRepository {

    private JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceStatusService.class).logger();

    private static final String SQL_STATUT_DE_NIVEAU_REMISE = "select * from virinst1a.fct_feedback_masse_restitution_statut_niveau_ordre(?)";

    private static final String SQL_STATUT_DE_NIVEAU_OPERATION = "select * from virinst1a.fct_feedback_masse_restitution_statut_niveau_operation(?,?,?)";

    public DatabaseRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RemittanceRequest findByRemittanceOrderId(String remittanceOrderId) {
        LOGGER.info("Execute query - RemittanceDatabaseRespository: fct_feedback_masse_restitution_statut_niveau_remise:");

        return jdbcTemplate.queryForObject(SQL_STATUT_DE_NIVEAU_REMISE,
                new RemittanceRequestMapper(), remittanceOrderId);
    }

    public Page<RemittanceOperations> findByRemittanceOrderIdPagination(String remittanceOrderId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        LOGGER.info("Execute query - RemittanceDatabaseRespository: fct_feedback_masse_restitution_statut_niveau_operation");

        List<RemittanceOperations> remittanceOperationsList = jdbcTemplate.query(SQL_STATUT_DE_NIVEAU_OPERATION, new RemittanceOperationsMapper(),
                remittanceOrderId, page, size);

        long totalCount = extractTotalCount(remittanceOperationsList);

        LOGGER.info("Execute query - fct_Remittance_total_operation: total: {} ", totalCount);

        return new PageImpl<>(remittanceOperationsList, pageable, totalCount);
    }

    private long extractTotalCount(List<RemittanceOperations> operations) {
        return operations.isEmpty() ? 0 : operations.get(0).totalCount();
    }

}
