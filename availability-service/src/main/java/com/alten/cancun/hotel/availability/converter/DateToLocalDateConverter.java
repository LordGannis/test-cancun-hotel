package com.alten.cancun.hotel.availability.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter
public class DateToLocalDateConverter implements AttributeConverter<Date, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(Date attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toLocalDate();
    }

    @Override
    public Date convertToEntityAttribute(LocalDate dbData) {
        if (dbData == null) {
            return null;
        }
        return Date.valueOf(dbData);
    }
}
