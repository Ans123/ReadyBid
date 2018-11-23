package net.readybid.api.main.rfp.questionnaire.validators;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class ValidatorFactory {

    public static QuestionValidator getMaxLength(int length) {
        return new MaxLengthValidator(length);
    }

    public static QuestionValidator getIn(String list) {
        return new InValidator(list);
    }

    public static QuestionValidator getMax(long max) {
        return new MaxValidator(max);
    }

}
