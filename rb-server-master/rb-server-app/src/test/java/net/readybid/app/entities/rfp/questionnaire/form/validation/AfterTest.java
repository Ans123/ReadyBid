package net.readybid.app.entities.rfp.questionnaire.form.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: After")
class AfterTest {

    private After sut;

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
            sut = new After(fieldWithAnswersPrefix, false);
        }

        @Test
        void andAnswerIsBeforeFieldAnswerIsValidShouldBeFalse(){
            answer = "2017-12-31";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsNotInclusiveIsValidShouldBeFalse(){
            answer = "2018-01-01";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(fieldWithAnswersPrefix, true);
            answer = "2018-01-01";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(fieldWithAnswersPrefix, true);
            answer = "2018-01-02";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsBeforeFieldAnswerAndAfterIsInclusiveIsValidShouldBeFalse(){
            sut = new After(fieldWithAnswersPrefix, true);
            answer = "2017-12-31";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerIsValidShouldBeTrue(){
            answer = "2018-01-02";
            assertTrue(mut.get());
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
            answer = "2017-12-31";
            assertTrue(mut.get());
        }

        @Test
        void andAnswersAnswerIsNotParsableDateIsValidShouldBeTrue(){
            answers.put(field, "text");
            answer = "2017-12-31";
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
            sut = new After(fieldWithContextPrefix, false);
        }

        @Test
        void andAnswerIsBeforeContextFieldAnswerIsValidShouldBeFalse(){
            answer = "2018-12-31";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToContextFieldAnswerAndAfterIsNotInclusiveIsValidShouldBeFalse(){
            answer = "2019-01-01";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToContextFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(fieldWithContextPrefix, true);
            answer = "2019-01-01";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsAfterContextFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(fieldWithContextPrefix, true);
            answer = "2019-01-02";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsBeforeContextFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(fieldWithContextPrefix, true);
            answer = "2018-12-31";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsAfterContextFieldAnswerIsValidShouldBeTrue(){
            answer = "2019-01-02";
            assertTrue(mut.get());
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
            answer = "2018-31-12";
            assertTrue(mut.get());
        }

        @Test
        void andContextAnswerIsNotParsableDateIsValidShouldBeTrue(){
            context.put(field, "text");
            answer = "2018-31-12";
            assertTrue(mut.get());
        }
    }

    @Nested
    @DisplayName("When Field is Value")
    class WhenFieldIsValue {

        private String field = "2017-01-01";

        @BeforeEach
        void setup(){
            sut = new After(field, false);
        }

        @Test
        void andAnswerIsBeforeFieldAnswerIsValidShouldBeFalse(){
            answer = "2016-12-31";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsNotInclusiveIsValidShouldBeFalse(){
            answer = "2017-01-01";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualToFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(field, true);
            answer = "2017-01-01";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerAndAfterIsInclusiveIsValidShouldBeTrue(){
            sut = new After(field, true);
            answer = "2017-01-02";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsBeforeFieldAnswerAndAfterIsInclusiveIsValidShouldBeFalse(){
            sut = new After(field, true);
            answer = "2016-12-31";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsAfterFieldAnswerIsValidShouldBeTrue(){
            answer = "2017-01-02";
            assertTrue(mut.get());
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
            sut = new After(null, false);
            answer = "2017-31-12";
            assertTrue(mut.get());
        }

        @Test
        void andFieldIsNotParsableDateIsValidShouldBeTrue(){
            sut = new After("text", false);
            answer = "2017-31-12";
            assertTrue(mut.get());
        }
    }
}
