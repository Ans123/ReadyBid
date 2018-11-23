package net.readybid.api.main.rfp.questionnaire.parsers;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ToDecimalParser implements QuestionParser {

    @Override
    public Object parse(Object value) {
        if(value == null) return null;
        if(value instanceof Double) {
            return value;
        } else {
            String val = value.toString().trim();
            if(val.isEmpty()) return null;
            //val = val.replaceAll("[\\D.]", "");
            return Double.parseDouble(val);
        }
    }

    @Override
    public String getParseErrorCode() {
        return "ParseDecimal";
    }
}
