package net.readybid.api.main.rfp.questionnaire.validators;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public interface QuestionValidator {
    boolean isNotValid(Object value) throws Exception;
    String getErrorCode();
}
