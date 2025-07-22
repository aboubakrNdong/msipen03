package com.cl.msipen03.mapper;

import com.cl.msipen03.entities.RemittanceRequest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RemittanceRequestMapper implements RowMapper<RemittanceRequest> {

    @Override
    public RemittanceRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RemittanceRequest(
                rs.getTimestamp("timcre_psr2"),
                rs.getString("id_ordpmt_cl"),
                rs.getString("codetagrpord_psr2"),
                rs.getString("libnomdbt"),
                rs.getInt("numprsorgdbt"),
                rs.getInt("nbrtrnlot"),
                rs.getTimestamp("timreaordvir"),
                rs.getBigDecimal("mntlot")
        );
    }
}