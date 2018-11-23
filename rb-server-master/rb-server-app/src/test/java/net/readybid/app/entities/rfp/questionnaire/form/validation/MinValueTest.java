package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: Min Value")
class MinValueTest {

    private MinValue sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new MinValue(BigDecimal.ONE);
    }

    @Test
    void whenAnswerIsNullIsValidShouldBeTrue(){
        answer = null;
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsEmptyIsValidShouldBeTrue(){
        answer = "";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsLessThanMinIsValidShouldBeFalse(){
        answer = "0";
        assertFalse(mut.get());
    }

    @Test
    void whenAnswerIsEqualToMinIsValidShouldBeTrue(){
        answer = "1";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsGreaterThanMinIsValidShouldBeTrue(){
        answer = "2";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsNotANumberIsValidShouldBeFalse(){
        answer = "text";
        assertFalse(mut.get());
    }

    @Test
    void whenMinIsNullIsValidShouldBeTrue(){
        sut = new MinValue(null);
        answer = String.valueOf(RbRandom.randomDouble());
        assertTrue(mut.get());
    }
}