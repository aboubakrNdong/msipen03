package com.cl.msipen03.utils;

import com.cl.msipen03.entities.VirementStatusEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Utils {

    public static String statusMapping(ResultSet rs, String columnName) throws SQLException {
        String statusCode = rs.getString(columnName);
        return VirementStatusEnum.getStatusLabel(statusCode);
    }

    public static String transformsTimeStampIntoString(Timestamp ts) {
        return (ts != null)
                ? ts.toLocalDateTime().toString()
                : "";
    }

}