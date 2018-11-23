package net.readybid.api.main.rfp.questionnaire.parsers;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ToStringParser implements QuestionParser {
    private static final String doubleFormat = "%.0f";
    private static final String errorCode = "ParseString";

    private static Object staticParse(Object value){
        if(value == null) return null;
        if(value instanceof Double){
            return String.format(doubleFormat, (Double)value);
        }
        String val = value.toString().trim();
        if(val.isEmpty()) return null;
        return val;
    }

    @Override
    public Object parse(Object value) { return staticParse(value); }

    @Override
    public String getParseErrorCode() {
        return errorCode;
    }
}