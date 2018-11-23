package net.readybid.mongodb;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String arg0) {
        return LocalDate.parse(arg0, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
