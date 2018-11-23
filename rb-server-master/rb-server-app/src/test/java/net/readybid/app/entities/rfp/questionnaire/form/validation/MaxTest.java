package net.readybid.app.entities.rfp.questionnaire.form.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: Max")
class MaxTest {

    private Max sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new Max(5);
    }

    @Test
    void whenAnswerIsNullIsValidShouldBeTrue(){
        answer = null;
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerLengthIsLessThanMaxIsValidShouldBeTrue(){
        answer = "123";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerLengthIsEqualToMaxIsValidShouldBeTrue(){
        answer = "12345";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerLengthIsGreaterThanMaxIsValidShouldBeFalse(){
        answer = "123456";
        assertFalse(mut.get());
    }

    @Test
    void whenMaxIsLessThanOneAndAnswerIsNullIsValidShouldBeTrue(){
        sut = new Max(0);
        answer = null;
        assertTrue(mut.get());
    }

    @Test
    void whenMaxIsLessThanOneAndAnswerIsEmptyIsValidShouldBeTrue(){
        sut = new Max(0);
        answer = "";
        assertTrue(mut.get());
    }

    @Test
    void whenMaxIsLessThanOneAndAnswerIsNotEmptyIsValidShouldBeFalse(){
        sut = new Max(0);
        answer = "123";
        assertFalse(mut.get());
    }
}
