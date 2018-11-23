package net.readybid.api.main.rfp.questionnaire.parsers;

import org.springframework.util.StringUtils;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ToListParser implements QuestionParser {

    private static final String errorCode = "ParseList";

    public Object staticParse(Object value) {
        if(value == null) return null;
        String val = value.toString().trim();
        if(val.isEmpty()) return null;
        return StringUtils.capitalize(val);
    }

    @Override
    public Object parse(Object value) {
        return staticParse(value);
    }

    @Override
    public String getParseErrorCode() {
        return errorCode;
    }
}
