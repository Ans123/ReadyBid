package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.test_utils.RbAbstractAssert;

public class QuestionnaireQuestionImplAssert extends RbAbstractAssert<QuestionnaireQuestionImplAssert, QuestionnaireQuestionImpl> {

    public static QuestionnaireQuestionImplAssert that(QuestionnaireQuestionImpl actual) {
        return new QuestionnaireQuestionImplAssert(actual);
    }

    private QuestionnaireQuestionImplAssert(QuestionnaireQuestionImpl actual) {
        super(actual, QuestionnaireQuestionImplAssert.class);
    }

    public QuestionnaireQuestionImplAssert hasId(Object expected) {
        assertFieldEquals("id", expected, actual.id);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasName(Object expected) {
        assertFieldEquals("name", expected, actual.name);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasClasses(Object expected) {
        assertFieldEquals("classes", expected, actual.classes);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasOrd(Object expected) {
        assertFieldEquals("ord", expected, actual.ord);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasPlaceholder(Object expected) {
        assertFieldEquals("placeholder", expected, actual.placeholder);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasDescription(Object expected) {
        assertFieldEquals("description", expected, actual.description);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasReadOnly(Object expected) {
        assertFieldEquals("read only", expected, actual.readOnly);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasLocked(Object expected) {
        assertFieldEquals("locked", expected, actual.locked);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasRequired(Object expected) {
        assertFieldEquals("required", expected, actual.required);
        return this;
    }

    public QuestionnaireQuestionImplAssert hasValidator(Object expected) {
        assertFieldEquals("validator", expected, actual.validator);
        return this;
    }
}
