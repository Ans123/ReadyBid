package net.readybid.app.core.entities.rfp;

import net.readybid.test_utils.RbAbstractAssert;

public class QuestionnaireResponseAssert extends RbAbstractAssert<QuestionnaireResponseAssert, QuestionnaireResponse> {

    public static QuestionnaireResponseAssert that(QuestionnaireResponse actual) {
        return new QuestionnaireResponseAssert(actual);
    }

    private QuestionnaireResponseAssert(QuestionnaireResponse actual) {
        super(actual, QuestionnaireResponseAssert.class);
    }

    public QuestionnaireResponseAssert isValid(Object expected) {
        assertFieldEquals("is valid", expected, actual.isValid());
        return this;
    }

    public QuestionnaireResponseAssert hasErrorsCount(Object expected) {
        assertFieldEquals("errors count", expected, actual.getErrorsCount());
        return this;
    }

    public QuestionnaireResponseAssert hasAnswers(Object expected) {
        assertFieldEquals("answers", expected, actual.getAnswers());
        return this;
    }

    public QuestionnaireResponseAssert hasState(Object expected) {
        assertFieldEquals("state", expected, actual.getState());
        return this;
    }

}