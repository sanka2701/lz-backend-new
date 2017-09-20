package sk.liptovzije.utils;

import org.joda.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

/**
 * Created by husenica on 17.9.16.
 */
@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate,Date> {
    public Date convertToDatabaseColumn(LocalDate date) {
        if(date==null) return null;
        return new Date(date.toDateTimeAtStartOfDay().getMillis());
    }

    public LocalDate convertToEntityAttribute(Date date) {
        if(date==null) return null;
        return new LocalDate(date.getTime());
    }
}
