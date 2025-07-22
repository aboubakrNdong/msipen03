package com.cl.msipen03.repository;

import javax.sql.DataSource;

import com.cl.msipen03.entities.RemittanceOperation;
import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.mapper.RemittanceOperationsListMapper;
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

    private static final String SQL_STATUT_DE_NIVEAU_REMISE = "select * from virinst1a.fct_feedback_masse_restitution_statut_niveau_remise(?)";

    //TODO: fais la jointure pour renvoyer le bon libelle de code d'erreur
    private static final String SQL_STATUT_DE_NIVEAU_OPERATION = "select * from virinst1a.fct_feedback_masse_restitution_statut_niveau_operation(?,?,?)";

    public DatabaseRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RemittanceRequest findByRemittanceOrderId(String remittanceOrderId) {
        LOGGER.info("Execute query - RemittanceDatabaseRespository: fct_feedback_masse_restitution_statut_niveau_remise:");

        return jdbcTemplate.queryForObject(SQL_STATUT_DE_NIVEAU_REMISE,
                new RemittanceRequestMapper(), remittanceOrderId);
    }

    public Page<RemittanceOperation> findByRemittanceOrderIdPagination(String remittanceOrderId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        LOGGER.info("Execute query - RemittanceDatabaseRespository: fct_feedback_masse_restitution_statut_niveau_operation");

        List<RemittanceOperation> remittanceOperationList = jdbcTemplate.query(SQL_STATUT_DE_NIVEAU_OPERATION, new RemittanceOperationsListMapper(),
                remittanceOrderId, page, size);

        long totalCount = extractTotalCount(remittanceOperationList);

        LOGGER.info("Execute query - fct_Remittance_total_operation: total: {} ", totalCount);

        return new PageImpl<>(remittanceOperationList, pageable, totalCount);
    }

    private long extractTotalCount(List<RemittanceOperation> operations) {
        return operations.isEmpty() ? 0 : operations.get(0).totalCount();
    }

}
