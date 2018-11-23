package net.readybid.api.main.rfp.questionnaire.parsers;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ParsersFactory {

    public static QuestionParser getToString() {
        return new ToStringParser();
    }

    public static QuestionParser getToList() {
        return new ToListParser();
    }

    public static QuestionParser getToInteger() {
        return new ToIntegerParser();
    }

    public static QuestionParser getToDate() {
        return new ToDateParser();
    }

    public static QuestionParser getToDecimal() {
        return new ToDecimalParser();
    }
}
