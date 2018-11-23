package net.readybid.app.entities.rfp.questionnaire.form.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: Between")
class DecimalTest {

    private Decimal sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new Decimal(3);
    }

    @Test
    void whenAnswerContainsLessThanMaxNumberOdDecimalsIsValidShouldBeTrue(){
        answer = "5.23";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerContainsMaxNumberOdDecimalsIsValidShouldBeTrue(){
        answer = "5.234";
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerContainsMoreThanMaxNumberOdDecimalsIsValidShouldBeTrue(){
        answer = "5.2345";
        assertFalse(mut.get());
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
    void whenAnswerIsNotANumberIsValidShouldBeFalse(){
        answer = "text";
        assertFalse(mut.get());
    }

    @Test
    void whenMaxDecimalsIsZeroAndAnswerIsNotWholeNumberIsValidShouldBeFalse(){
        sut = new Decimal(0);
        answer = "2.1";
        assertFalse(mut.get());
    }

    @Test
    void whenMaxDecimalsIsZeroAndAnswerIsWholeNumberIsValidShouldBeTrue(){
        sut = new Decimal(0);
        answer = "2.";
        assertTrue(mut.get());
    }

    @Test
    void whenMaxDecimalsIsNegativeAndAnswerIsNotWholeNumberIsValidShouldBeFalse(){
        sut = new Decimal(-1);
        answer = "2.1";
        assertFalse(mut.get());
    }

    @Test
    void whenMaxDecimalsIsNegativeAndAnswerIsWholeNumberIsValidShouldBeTrue(){
        sut = new Decimal(-1);
        answer = "2.";
        assertTrue(mut.get());
    }
}