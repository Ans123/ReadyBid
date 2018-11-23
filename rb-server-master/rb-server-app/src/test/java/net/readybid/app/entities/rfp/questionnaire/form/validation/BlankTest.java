package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Questionnaire Validator: Blank")
class BlankTest {

    private Blank sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @Nested
    @DisplayName("When Is Blank")
    class WhenIsRequired {

        @BeforeEach
        void setup(){
            sut = new Blank(true);
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
        void andAnswerIsNotEmptyIsValidShouldBeFalse(){
            answer = RbRandom.alphanumeric(100);
            assertFalse(mut.get());
        }
    }

    @Nested
    @DisplayName("When Is Not Blank")
    class WhenIsNotRequired {

        @BeforeEach
        void setup(){
            sut = new Blank(false);
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