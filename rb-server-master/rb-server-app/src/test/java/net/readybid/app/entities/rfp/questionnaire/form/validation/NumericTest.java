package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Questionnaire Validator: Numeric")
class NumericTest {

    private Numeric sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @Nested
    @DisplayName("When Is Numeric")
    class WhenIsNumeric {

        @BeforeEach
        void setup(){
            sut = new Numeric(true);
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
        void andAnswerIsANumberIsValidShouldBeTrue(){
            answer = String.valueOf(RbRandom.randomDouble());
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotANumberIsValidShouldBeFalse(){
            answer = RbRandom.randomAlphabetic(10);
            assertFalse(mut.get());
        }
    }

    @Nested
    @DisplayName("When Is Not Numeric")
    class WhenIsNotNumeric{

        @BeforeEach
        void setup(){
            sut = new Numeric(false);
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
        void andAnswerIsANumberIsValidShouldBeTrue(){
            answer = String.valueOf(RbRandom.randomDouble());
            assertTrue(mut.get());
        }

        @Test
        void andAnswerIsNotANumberIsValidShouldBeTrue(){
            answer = RbRandom.randomAlphabetic(10);
            assertTrue(mut.get());
        }
    }
}