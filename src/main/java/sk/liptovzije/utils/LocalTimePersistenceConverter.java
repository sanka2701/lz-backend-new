package sk.liptovzije.utils;

import org.joda.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;

/**
 * Created by husenica on 17.9.16.
 */
@Converter(autoApply = true)
public class LocalTimePersistenceConverter implements AttributeConverter<LocalTime,Time> {
    public Time convertToDatabaseColumn(LocalTime time) {
        if(time==null) return null;
        return new Time(time.toDateTimeToday().getMillis());
    }

    public LocalTime convertToEntityAttribute(Time time) {
        if(time==null) return null;
        return new LocalTime(time.getTime());
    }
}

