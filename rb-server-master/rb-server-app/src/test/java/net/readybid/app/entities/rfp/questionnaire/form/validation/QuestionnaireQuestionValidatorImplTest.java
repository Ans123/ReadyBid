package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Questionnaire Question Validator Impl")
class QuestionnaireQuestionValidatorImplTest {

    private QuestionnaireQuestionValidatorImpl sut;

    @Nested
    class IsValidTest{

        private QuestionnaireQuestion question;

        // arguments
        private Map<String,String> answers;
        private Map<String,String> context;

        // result
        private boolean result;

        // method
        private Runnable mut = () -> result = sut.isValid(answers, context);

        @Test
        void whenQuestionIsNotSet(){
            createValidator();
            createAnswers();
            isRuntimeExceptionThrown();
        }

        @Test
        void whenThereAreNoValidations(){
            createValidator();
            setValidatorQuestion();
            createAnswers();
            mut.run();
            isValid();
        }

        @Test
        void whenThereAreNoValidationsAndQuestionIsMandatoryAndAnswerIsNull(){
            createValidator();
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo(null);

            mut.run();

            isInvalid();
        }

        @Test
        void whenThereAreNoValidationsAndQuestionIsMandatoryAndAnswerIsEmpty(){
            createValidator();
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo("");

            mut.run();

            isInvalid();
        }


        @Test
        void whenThereAreNoValidationsAndQuestionIsMandatoryAndAnswerIsNotEmptyNorNull(){
            createValidator();
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo(RbRandom.alphanumeric());

            mut.run();

            isValid();
        }

        @Test
        void whenAnswerPassesAllValidations(){
            createValidator(withAllPassingValidations());
            setValidatorQuestion();
            createAnswers();

            mut.run();

            isValid();
        }

        @Test
        void whenAnswerPassesAllValidationsAndQuestionIsMandatory(){
            createValidator(withAllPassingValidations());
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo(RbRandom.alphanumeric());

            mut.run();

            isValid();
        }

        @Test
        void whenAnswerPassesAllValidationsAndQuestionIsMandatoryAndAnswerIsNull(){
            createValidator(withAllPassingValidations());
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo(null);

            mut.run();

            isInvalid();
        }

        @Test
        void whenAnswerPassesAllValidationsAndQuestionIsMandatoryAndAnswerIsEmpty(){
            createValidator(withAllPassingValidations());
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo("");

            mut.run();

            isInvalid();
        }

        @Test
        void whenAnswerFailsFirstValidation(){
            createValidator(withFailingFirstValidation());
            setValidatorQuestion();
            createAnswers();

            mut.run();

            isInvalid();
        }

        @Test
        void whenAnswerFailsLastValidation(){
            createValidator(withFailingLastValidation());
            setValidatorQuestion();
            createAnswers();

            mut.run();

            isInvalid();
        }

        @Test
        void whenAnswerFailsAnyValidation(){
            createValidator(withFailingAnyValidation());
            setValidatorQuestion();
            createAnswers();

            mut.run();

            isInvalid();
        }

        @Test
        void whenAnswerFailsAnyValidationAndQuestionIsMandatoryAndAnswerIsNotNullNorEmpty(){
            createValidator(withFailingAnyValidation());
            setValidatorQuestion();
            makeQuestionMandatory();
            createAnswers();
            setQuestionAnswerTo("");

            mut.run();

            isInvalid();
        }

        @Test
        void allValidationsAreTestedWithSuppliedArguments(){
            final String questionId = RbRandom.alphanumeric();
            final String answer = RbRandom.alphanumeric();
            answers = new HashMap<>();
            answers.put(questionId, answer);
            context = new HashMap<>();

            final List<QuestionnaireQuestionValidation> validations = withAllPassingValidations();

            final QuestionnaireQuestion question = mock(QuestionnaireQuestion.class);
            doReturn(questionId).when(question).getId();

            sut = new QuestionnaireQuestionValidatorImpl(validations);
            sut.setQuestion(question);

            mut.run();

            validations.forEach(
                    v -> verify(v, times(1))
                            .isValid(same(answer), same(answers), same(context)));
        }

        private void createValidator(){
            sut = new QuestionnaireQuestionValidatorImpl();
        }

        private void createValidator(List<QuestionnaireQuestionValidation> validations) {
            sut = new QuestionnaireQuestionValidatorImpl(validations);
        }

        private void setValidatorQuestion(){
            question = mock(QuestionnaireQuestion.class);
            sut.setQuestion(question);
        }

        private void makeQuestionMandatory(){
            doReturn(true).when(question).isRequired();
        }

        void createAnswers(){
            answers = new HashMap<>();
        }

        void setQuestionAnswerTo(String value){
            final String questionId = RbRandom.alphanumeric(10);
            doReturn(questionId).when(question).getId();
            answers.put(questionId, value);
        }

        private List<QuestionnaireQuestionValidation> withAllPassingValidations() {
            final int validationsLength = RbRandom.randomInt(5,20);
            return IntStream.range(0, validationsLength)
                    .mapToObj(i -> passingValidation())
                    .collect(Collectors.toList());
        }

        private List<QuestionnaireQuestionValidation> withFailingFirstValidation() {
            final List<QuestionnaireQuestionValidation> vs = withAllPassingValidations();
            vs.set(0, failingValidation());
            return vs;
        }

        private List<QuestionnaireQuestionValidation> withFailingLastValidation() {
            final List<QuestionnaireQuestionValidation> vs = withAllPassingValidations();
            vs.add(failingValidation());
            return vs;
        }

        private List<QuestionnaireQuestionValidation> withFailingAnyValidation() {
            final List<QuestionnaireQuestionValidation> vs = withAllPassingValidations();
            final int failingValidationIndex = RbRandom.randomInt(1, vs.size()-1);
            vs.set(failingValidationIndex, failingValidation());
            return vs;
        }

        private QuestionnaireQuestionValidation passingValidation() {
            final boolean toBeReturned = true;
            return createValidation(toBeReturned);
        }

        private QuestionnaireQuestionValidation failingValidation() {
            final boolean toBeReturned = false;
            return createValidation(toBeReturned);
        }

        private QuestionnaireQuestionValidation createValidation(boolean toBeReturned) {
            final QuestionnaireQuestionValidation v = mock(QuestionnaireQuestionValidation.class);
            doReturn(toBeReturned).when(v).isValid(any(), any(), any());
            return v;
        }

        private void isValid() {
            assertTrue(result);
        }

        private void isInvalid() {
            assertFalse(result);
        }

        private void isRuntimeExceptionThrown() {
            assertThrows(RuntimeException.class, () -> mut.run());
        }
    }
}