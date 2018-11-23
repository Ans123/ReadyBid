package net.readybid.api.main.rfp.questionnaire.parsers;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public interface QuestionParser {
    Object parse(Object value) throws RuntimeException;
    String getParseErrorCode();
}
