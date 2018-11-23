package net.readybid.app.entities.rfp.questionnaire.form.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Questionnaire Validator: Before")
class BeforeTest {

    private Before sut;

    private String answer;

    private Map<String, String> answers = new HashMap<>();

    private Map<String, String> context = new HashMap<>();

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @Nested
    @DisplayName("When Field is Answers Key")
    class WhenFieldIsAnswersKey {

        private String field = "answer";
        private String fieldWithAnswersPrefix = "#answer";

        @BeforeEach
        void setup(){
            answers.put(field, "2018-01-01");
            sut = new Before(fieldWithAnswersPrefix, false);
        }

        @Test
        void andAnswerIsBeforeFieldAnswerIsValidShouldBeTrue(){
            answer = "2017-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsNotInclusiveIsValidShouldBeFalse(){
            answer = "2018-01-01";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new Before(fieldWithAnswersPrefix, true);
            answer = "2018-01-01";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsBeforeFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new Before(fieldWithAnswersPrefix, true);
            answer = "2017-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerAndAfterIsInclusiveIsValidShouldBeFalse(){
            sut = new Before(fieldWithAnswersPrefix, true);
            answer = "2018-01-02";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerIsValidShouldBeFalse(){
            answer = "2018-01-02";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsNullIsValidShouldBeTrue(){
            answer = null;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotParsableDateIsValidShouldBeTrue(){
            answer = "text";
            assertTrue(mut.get());
        }

        @Test
        void andAnswersAnswerIsNullIsValidShouldBeTrue(){
            answers.put(field, null);
            answer = "2018-01-02";
            assertTrue(mut.get());
        }

        @Test
        void andAnswersAnswerIsNotParsableDateIsValidShouldBeTrue(){
            answers.put(field, "text");
            answer = "2018-01-02";
            assertTrue(mut.get());
        }
    }

    @Nested
    @DisplayName("When Field is Context Key")
    class WhenFieldIsContextKey {

        private String field = "answer";
        private String fieldWithContextPrefix = "$answer";

        @BeforeEach
        void setup(){
            context.put(field, "2019-01-01");
            sut = new Before(fieldWithContextPrefix, false);
        }

        @Test
        void andAnswerIsBeforeContextFieldAnswerIsValidShouldBeTrue(){
            answer = "2018-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsEqualToContextFieldAnswerAndAfterIsNotInclusiveIsValidShouldBeFalse(){
            answer = "2019-01-01";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToContextFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new Before(fieldWithContextPrefix, true);
            answer = "2019-01-01";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsBeforeContextFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new Before(fieldWithContextPrefix, true);
            answer = "2018-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsAfterContextFieldAnswerAndAfterIsInclusiveIsValidShouldBeFalse(){
            sut = new Before(fieldWithContextPrefix, true);
            answer = "2019-01-02";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsAfterContextFieldAnswerIsValidShouldBeTrue(){
            answer = "2019-01-02";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsNullIsValidShouldBeTrue(){
            answer = null;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotParsableDateIsValidShouldBeTrue(){
            answer = "text";
            assertTrue(mut.get());
        }

        @Test
        void andContextAnswerIsNullIsValidShouldBeTrue(){
            context.put(field, null);
            answer = "2019-01-02";
            assertTrue(mut.get());
        }

        @Test
        void andContextAnswerIsNotParsableDateIsValidShouldBeTrue(){
            context.put(field, "text");
            answer = "2019-01-02";
            assertTrue(mut.get());
        }
    }

    @Nested
    @DisplayName("When Field is Value")
    class WhenFieldIsValue {

        private String field = "2017-01-01";

        @BeforeEach
        void setup(){
            sut = new Before(field, false);
        }

        @Test
        void andAnswerIsBeforeFieldAnswerIsValidShouldBeTrue(){
            answer = "2016-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsNotInclusiveIsValidShouldBeFalse(){
            answer = "2017-01-01";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new Before(field, true);
            answer = "2017-01-01";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsBeforeFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new Before(field, true);
            answer = "2016-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerAndAfterIsInclusiveIsValidShouldBeFalse(){
            sut = new Before(field, true);
            answer = "2017-01-02";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerIsValidShouldBeFalse(){
            answer = "2017-01-02";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsNullIsValidShouldBeTrue(){
            answer = null;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotParsableDateIsValidShouldBeTrue(){
            answer = "text";
            assertTrue(mut.get());
        }

        @Test
        void andFieldIsNullIsValidShouldBeTrue(){
            sut = new Before(null, false);
            answer = "2017-01-02";
            assertTrue(mut.get());
        }

        @Test
        void andFieldIsNotParsableDateIsValidShouldBeTrue(){
            sut = new Before("text", false);
            answer = "2017-01-02";
            assertTrue(mut.get());
        }
    }
}