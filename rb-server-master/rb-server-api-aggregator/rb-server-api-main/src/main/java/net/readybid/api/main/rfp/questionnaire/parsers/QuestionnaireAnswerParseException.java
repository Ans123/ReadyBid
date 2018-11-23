package net.readybid.api.main.rfp.questionnaire.parsers;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class QuestionnaireAnswerParseException extends RuntimeException {
    private final String errorCode;

    public QuestionnaireAnswerParseException(String errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
