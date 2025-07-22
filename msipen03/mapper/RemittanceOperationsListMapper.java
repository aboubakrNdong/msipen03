package com.cl.msipen03.mapper;

import com.cl.msipen03.entities.RemittanceOperation;
import com.cl.msipen03.entities.VirementStatusEnum;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemittanceOperationsListMapper implements RowMapper<RemittanceOperation> {

    @Override
    public RemittanceOperation mapRow(ResultSet rs, int rowNum) throws SQLException {

        String statusCode = rs.getString("codetagrptrn_psr2");
        String statusLabel = VirementStatusEnum.getStatusLabel(statusCode);

        return new RemittanceOperation(
                rs.getString("libnombnf"),
                rs.getString("libnomdbtpcp_op"),
                rs.getString("codbicisobnf"),
                rs.getString("cod_inc_bnf"),
                rs.getTimestamp("timreaordvir"),
                rs.getString("libmotpmt"),
                rs.getString("librefopr"),
                rs.getBigDecimal("mntvireur"),
                statusLabel,
                rs.getString("rej_long_desc"),
                rs.getLong("total_count")
        );
    }
}