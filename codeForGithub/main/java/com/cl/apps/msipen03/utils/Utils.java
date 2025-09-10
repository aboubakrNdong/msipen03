package com.cl.apps.msipen03.utils;

import com.cl.apps.msipen03.entities.RemittanceStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.poi.ss.usermodel.*;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.sql.Date;

public class Utils {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Converter
    public static class StatusAttributeConverter implements AttributeConverter<String, String> {
        @Override
        public String convertToDatabaseColumn(String attribute) {
            return attribute;
        }

        @Override
        public String convertToEntityAttribute(String dbData) {
            return RemittanceStatusEnum.getStatusLabel(dbData);
        }
    }

    public static String checkDateToAvoidNPE(Date date, DateTimeFormatter formatter) {
        return Optional.ofNullable(date)
                .map(d -> d.toLocalDate().format(formatter))
                .orElse("");
    }

    public static String checkStringValueToAvoidNPE(Object value) {
        return Optional.ofNullable(value)
                .map(Object::toString)
                .orElse("");
    }

}