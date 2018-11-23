package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Questionnaire Validator: Equal")
class EqualTest {

    private Equal sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @Nested
    @DisplayName("When Is Equal")
    class WhenIsEqual {

        private String testValue = "test";
        private final boolean isEqual = true;

        @BeforeEach
        void setup(){
            sut = new Equal(testValue, isEqual);
        }

        @Test
        void andAnswerIsNullIsValidShouldBeFalse(){
            answer = null;
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEmptyIsValidShouldBeFalse(){
            answer = "";
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsNotEqualIsValidShouldBeFalse(){
            answer = RbRandom.alphanumeric(100, true);
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEqualIsValidShouldBeTrue(){
            answer = testValue;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNullAndTestValueIsNullIsValidShouldBeTrue(){
            sut = new Equal(null, isEqual);
            answer = null;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotNullAndTestValueIsNullIsValidShouldBeFalse(){
            sut = new Equal(null, isEqual);
            answer = testValue;
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsEmptyAndTestValueIsEmptyIsValidShouldBeTrue(){
            sut = new Equal("", isEqual);
            answer = "";
            assertTrue(mut.get());
        }
    }
    @Nested
    @DisplayName("When Is Not Equal")
    class WhenIsNotEqual {

        private String testValue = "test";
        private final boolean isEqual = false;

        @BeforeEach
        void setup(){
            sut = new Equal(testValue, isEqual);
        }

        @Test
        void andAnswerIsNullIsValidShouldBeTrue(){
            answer = null;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsEmptyIsValidShouldBeTrue(){
            answer = "";
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotEqualIsValidShouldBeTrue(){
            answer = RbRandom.alphanumeric(100, true);
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsEqualIsValidShouldBeFalse(){
            answer = testValue;
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsNullAndTestValueIsNullIsValidShouldBeFalse(){
            sut = new Equal(null, isEqual);
            answer = null;
            assertFalse(mut.get());
        }

        @Test
        void andAnswerIsNotNullAndTestValueIsNullIsValidShouldBeTrue(){
            sut = new Equal(null, isEqual);
            answer = testValue;
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsEmptyAndTestValueIsEmptyIsValidShouldBeFalse(){
            sut = new Equal("", isEqual);
            answer = "";
            assertFalse(mut.get());
        }
    }
}