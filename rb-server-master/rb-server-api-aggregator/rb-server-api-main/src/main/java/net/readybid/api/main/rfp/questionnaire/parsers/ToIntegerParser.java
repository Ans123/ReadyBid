package net.readybid.api.main.rfp.questionnaire.parsers;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ToIntegerParser implements QuestionParser {

    private static final String errorCode = "ParseInteger";

    private static Object staticParse(Object value) {
        if(value == null) return null;
        if(value instanceof Double){
            return ((Double) value).longValue();
        } else {
            String val = value.toString().trim();
            if(val.isEmpty()) return null;
            val = val.split("\\.")[0];
            //val = val.replaceAll("[\\D]", "");
            return Long.parseLong(val);
        }
    }


    @Override
    public Object parse(Object value) { return staticParse(value);}

    @Override
    public String getParseErrorCode() {
        return errorCode;
    }
}
