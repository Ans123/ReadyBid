package net.readybid.app.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface DateHelper {

    static String customDateFormat(LocalDate date) {
        if (date == null) return null;
        String format = "d";
        final String tempDate = date.format(DateTimeFormatter.ofPattern(format));
        if (tempDate.endsWith("1") && !tempDate.endsWith("11"))
            format = "MMMM d'st', yyyy";
        else if (tempDate.endsWith("2") && !tempDate.endsWith("12"))
            format = "MMMM d'nd', yyyy";
        else if (tempDate.endsWith("3") && !tempDate.endsWith("13"))
            format = "MMMM d'rd', yyyy";
        else
            format = "MMMM d'th', yyyy";
        return date.format(DateTimeFormatter.ofPattern(format));
    }

}
