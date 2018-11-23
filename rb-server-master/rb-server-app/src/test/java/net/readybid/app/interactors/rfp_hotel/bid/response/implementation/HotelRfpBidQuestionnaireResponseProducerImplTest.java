package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.*;
import net.readybid.app.interactors.rfp.gate.QuestionnaireTemplateLibrary;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.test_utils.RbMapAssert;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class HotelRfpBidQuestionnaireResponseProducerImplTest {

    private HotelRfpBidQuestionnaireResponseProducerImpl sut;

    private QuestionnaireTemplateLibrary questionnaireTemplateLibrary;
    private HotelRfpBidResponseStateProducer stateProducer;
    private HotelRfpBidResponseValidator validator;

    @BeforeEach
    void setup(){
        generateSUTDependenciesAsMocks();
        createSUT();
    }

    @Nested
    class prepareResponseTest {

        private Map<String, String> answers;
        private HotelRfpBid bid;

        private QuestionnaireResponse result;
        private Runnable mut = () -> result = sut.prepareResponse(answers, bid);

        private QuestionnaireForm template;
        private QuestionnaireForm form;
        private List<QuestionnaireConfigurationItem> state;
        private Questionnaire questionnaire;

        @BeforeEach
        void setup(){
            generateDefaultMUTDependencies();
        }

        private void generateDefaultMUTDependencies() {
            answers = new HashMap<>();
            answers.put("test", "Test");
            bid = mock(HotelRfpBid.class);
        }

        @Test
        void whenThereAreNoErrorsAndRateIsProvided(){
            answers.put("LRA_S1_RT1_SGL", "20");
            templateLibraryReturnsTemplate();
            bidReturnsQuestionnaire();
            mergeTemplateAndQuestionnaireIntoForm();
            final ArgumentCaptor<Map<String, String>> contextCaptor = formReturnsNoErrors();
            stateProducerCreatesState();

            mut.run();

            QuestionnaireResponseAssert.that(result)
                    .isValid(true)
                    .hasErrorsCount(0L)
                    .hasAnswers(answers)
                    .hasState(state);

            isContextHavingProgramDates(contextCaptor.getValue());
        }

        @Test
        void whenThereAreAnswerErrorsAndRateIsProvided(){
            answers.put("LRA_S1_RT1_SGL", "20");
            templateLibraryReturnsTemplate();
            bidReturnsQuestionnaire();
            mergeTemplateAndQuestionnaireIntoForm();
            final ArgumentCaptor<Map<String, String>> contextCaptor = formReturnsErrors();
            stateProducerCreatesState();

            mut.run();

            QuestionnaireResponseAssert.that(result)
                    .isValid(false)
                    .hasErrorsCount(2L)
                    .hasAnswers(answers)
                    .hasState(state);

            isContextHavingProgramDates(contextCaptor.getValue());
        }

        @Test
        void whenAnswersIsNull(){
            answers = null;
            mut.run();
            isResultNull();
        }

        @Test
        void whenAnswersIsEmpty(){
            answers = new HashMap<>();
            mut.run();
            isResultNull();
        }

        @Test
        void whenBidIsNull(){
            bid = null;
            mut.run();
            isResultNull();
        }

        private void isResultNull() {
            assertNull(result);
        }

        private void isContextHavingProgramDates(Map<String, String> context) {
            RbMapAssert.that(context)
                    .contains("programStartDate", String.valueOf(bid.getProgramStartDate()))
                    .contains("programEndDate", String.valueOf(bid.getProgramEndDate()));
        }

        private void stateProducerCreatesState() {
            state = new ArrayList<>();
            doReturn(state).when(stateProducer).recreateState(same(answers));
        }

        private ArgumentCaptor<Map<String, String>> formReturnsNoErrors() {
            //noinspection unchecked
            final ArgumentCaptor<Map<String, String>> contextCaptor = ArgumentCaptor.forClass(Map.class);
            doReturn(new ArrayList<>())
                    .when(form).getErrors(same(answers), contextCaptor.capture());
            return contextCaptor;
        }

        private ArgumentCaptor<Map<String, String>> formReturnsErrors() {
            //noinspection unchecked
            final ArgumentCaptor<Map<String, String>> contextCaptor = ArgumentCaptor.forClass(Map.class);
            final List<String> errors = new ArrayList<>(Arrays.asList("error 1", "error 2"));
            doReturn(errors)
                    .when(form).getErrors(same(answers), contextCaptor.capture());
            return contextCaptor;
        }

        private void mergeTemplateAndQuestionnaireIntoForm() {
            form = mock(QuestionnaireForm.class);
            doReturn(form).when(template).merge(same(questionnaire));
        }

        private void bidReturnsQuestionnaire() {
            questionnaire = mock(Questionnaire.class);
            doReturn(questionnaire).when(bid).getQuestionnaire();
            doReturn(RbRandom.localDate()).when(bid).getProgramStartDate();
            doReturn(RbRandom.localDate()).when(bid).getProgramEndDate();
        }

        private void templateLibraryReturnsTemplate() {
            template = mock(QuestionnaireForm.class);
            doReturn(template).when(questionnaireTemplateLibrary).getTemplate();
        }
    }

    private void generateSUTDependenciesAsMocks() {
        questionnaireTemplateLibrary = mock(QuestionnaireTemplateLibrary.class);
        stateProducer = mock(HotelRfpBidResponseStateProducer.class);
        validator = new HotelRfpBidResponseValidator();
    }

    private void createSUT() {
        sut = new HotelRfpBidQuestionnaireResponseProducerImpl(questionnaireTemplateLibrary, stateProducer, validator);
    }
}