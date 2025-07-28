package com.cl.msipen03.mapper;

import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.entities.VirementStatusEnum;
import com.cl.msipen03.utils.Utils;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemittanceOperationsMapper implements RowMapper<RemittanceOperations> {

    @Override
    public RemittanceOperations mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new RemittanceOperations(
                rs.getString("libnombnf"),
                rs.getString("cod_inc_bnf"),
                rs.getString("codbicisobnf"),
                rs.getDate("timreaordvir"),
                rs.getString("libmotpmt"),
                rs.getString("librefopr"),
                rs.getString("id_refalttrn"),
                rs.getBigDecimal("mntvireur"),
                rs.getString("coddevitn"),
                rs.getString("id_oprvir_cl"),
                Utils.statusMapping(rs, "codetagrptrn_psr2"),
                rs.getString("rej_long_desc"),
                rs.getLong("total_count")
        );
    }
}