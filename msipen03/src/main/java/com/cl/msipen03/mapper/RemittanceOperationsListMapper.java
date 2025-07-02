package com.cl.msipen03.mapper;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RemittanceOperationsListMapper implements RowMapper<RemittanceOperationRequest> {

    @Override
    public RemittanceOperationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new RemittanceOperationRequest(
                rs.getString(1),
                rs.getString(2),
                rs.getLong(3)
        );
    }
}

