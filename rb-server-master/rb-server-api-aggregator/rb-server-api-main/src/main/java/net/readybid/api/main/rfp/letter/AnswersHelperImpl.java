package net.readybid.api.main.rfp.letter;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

@Service
public class AnswersHelperImpl implements AnswersHelper {

    @Override
    public String createPeriod(Object start, Object end) {
        try {
            final LocalDate sD = LocalDate.parse(String.valueOf(start));
            final LocalDate eD = LocalDate.parse(String.valueOf(end));
            return createPeriod(sD, eD);
        } catch (DateTimeParseException ex){
            return null;
        }
    }

    @Override
    public boolean readYesNo(Object yesOrNo) {
        return "Y".equalsIgnoreCase(String.valueOf(yesOrNo));
    }

    @Override
    public String readAmount(Object amount, String defaultValue) {
        return readAmount("$%.2f", amount, defaultValue);
    }

    private String readAmount(String format, Object amount, String defaultValue) {
        try {
            final Double d = Double.parseDouble(String.valueOf(amount));
            return String.format( format, d );
        } catch (NumberFormatException e){
            return defaultValue;
        }
    }

    @Override
    public boolean readAsFalse(Object obj, String falseValue) {
        return !falseValue.equals(String.valueOf(obj));
    }

    @Override
    public String readMixed(Object fixedOrPercentage, Object amount, String defaultValue) {
        final String fixedOrPercentageString = String.valueOf(fixedOrPercentage);
        String val;

        if("P".equalsIgnoreCase(fixedOrPercentageString)) {
            val = readPercentage(amount, defaultValue);
        } else if("F".equalsIgnoreCase(fixedOrPercentageString)){
            val = readAmount(amount, defaultValue);
        } else {
            val = defaultValue;
        }

        return val;
    }

    private String readPercentage(Object amount, String defaultValue) {
        return readAmount("%.2f%%", amount, defaultValue);
    }

    private String createPeriod(LocalDate startDate, LocalDate endDate){
        return createPeriod(startDate, endDate, FormatStyle.SHORT, Locale.US);
    }

    private String createPeriod(LocalDate startDate, LocalDate endDate, FormatStyle formatStyle, Locale locale) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(locale);
        return String.format("%s - %s", formatter.format(startDate), formatter.format(endDate));
    }
}
