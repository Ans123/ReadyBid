package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Questionnaire Validator: Max Value")
class MaxValueTest {

    private MaxValue sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new MaxValue(BigDecimal.ONE);
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
    void whenAnswerIsLessThanMaxIsValidShouldBeTrue(){
        answer = "0";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsEqualToMaxIsValidShouldBeTrue(){
        answer = "1";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsGreaterThanMaxIsValidShouldBeFalse(){
        answer = "2";
        assertFalse(mut.get());
    }

    @Test
    void whenAnswerIsNotANumberIsValidShouldBeFalse(){
        answer = "text";
        assertFalse(mut.get());
    }

    @Test
    void whenMaxIsNullIsValidShouldBeTrue(){
        sut = new MaxValue(null);
        answer = String.valueOf(RbRandom.randomDouble());
        assertTrue(mut.get());
    }
}