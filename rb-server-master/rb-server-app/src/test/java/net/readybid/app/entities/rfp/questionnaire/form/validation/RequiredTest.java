package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: Required")
class RequiredTest {

    private Required sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @Nested
    @DisplayName("When Is Required")
    class WhenIsRequired {

        @BeforeEach
        void setup(){
            sut = new Required(true);
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
        void andAnswerIsNotEmptyIsValidShouldBeTrue(){
            answer = RbRandom.alphanumeric(100);
            assertTrue(mut.get());
        }
    }

    @Nested
    @DisplayName("When Is Not Required")
    class WhenIsNotRequired {

        @BeforeEach
        void setup(){
            sut = new Required(false);
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
        void andAnswerIsNotEmptyIsValidShouldBeTrue(){
            answer = RbRandom.alphanumeric(100);
            assertTrue(mut.get());
        }
    }
}