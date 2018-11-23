package net.readybid.mongodb;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class LocalDateToStringConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(LocalDate arg0) {
        return arg0.toString();
    }
}
