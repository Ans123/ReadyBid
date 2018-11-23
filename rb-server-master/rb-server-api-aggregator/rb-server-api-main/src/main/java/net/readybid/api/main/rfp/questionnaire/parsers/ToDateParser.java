package net.readybid.api.main.rfp.questionnaire.parsers;

import org.apache.poi.ss.usermodel.DateUtil;

import java.text.SimpleDateFormat;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ToDateParser implements QuestionParser {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
    private static final String format = "%s-%s-%s";
    private final String errorCode = "ParseDate";

    @Override
    public Object parse(Object value) throws RuntimeException {
        if (value == null) return null;
        try {
            Double date = value instanceof Double ? (Double) value : Double.parseDouble(value.toString());
            return DateUtil.getJavaDate(date);
        } catch (Exception e) {
            try {
                String val = value.toString().trim();
                if(val.isEmpty()) return null;
                String separator = getSeparator(val);
                String parts[] = val.split(separator);
                String day, month, year;

                if (parts[2].length() == 4) { // AMERICAN
                    month = parts[0];
                    day = parts[1];
                    year = parts[2];
                } else { // ISO
                    year = parts[0];
                    month = parts[1];
                    day = parts[2];
                }
                return formatter.parse(String.format(format, day, month, year));
            } catch (Exception ne) {
                throw new QuestionnaireAnswerParseException(errorCode);
            }
        }
    }

    @Override
    public String getParseErrorCode() {
        return errorCode;
    }

    private String getSeparator(String val) {
        String separator;
        if(val.contains("-")){
            separator = "-";
        } else if(val.contains("/")){
            separator = "\\/";
        } else if(val.contains(".")){
            separator = "\\.";
        } else {
            separator = " ";
        }
        return separator;
    }
}
