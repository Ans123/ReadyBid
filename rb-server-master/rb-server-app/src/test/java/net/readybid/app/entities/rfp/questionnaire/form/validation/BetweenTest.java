package net.readybid.app.entities.rfp.questionnaire.form.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: Between")
class BetweenTest {

    private Between sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new Between(Arrays.asList(BigDecimal.ZERO, BigDecimal.TEN));
    }

    @Test
    void andAnswerIsBetweenIsValidShouldBeTrue(){
        answer = "5";
        assertTrue(mut.get());
    }

    @Test
    void andAnswerIsEqualToStartIsValidShouldBeTrue(){
        answer = "0";
        assertTrue(mut.get());
    }

    @Test
    void andAnswerIsEqualToEndIsValidShouldBeTrue(){
        answer = "10";
        assertTrue(mut.get());
    }

    @Test
    void andAnswerIsLowerThenStartIsValidShouldBeFalse(){
        answer = "-0.1";
        assertFalse(mut.get());
    }

    @Test
    void andAnswerIsGreaterThanEndIsValidShouldBeFalse(){
        answer = "10.1";
        assertFalse(mut.get());
    }

    @Test
    void andAnswerIsNullIsValidShouldBeTrue(){
        answer = null;
        assertTrue(mut.get());
    }

    @Test
    void andAnswerIsNotANumberIsValidShouldBeTrue(){
        answer = "text";
        assertTrue(mut.get());
    }

    @Test
    void andArgumentsListIsNullIsValidShouldBeTrue(){
        sut = new Between(null);
        answer = "5";
        assertTrue(mut.get());
    }

    @Test
    void andArgumentsListIsEmptyIsValidShouldBeTrue(){
        sut = new Between(new ArrayList<>());
        answer = "5";
        assertTrue(mut.get());
    }

    @Test
    void andArgumentsListHasOneArgumentIsValidShouldBeTrue(){
        sut = new Between(Arrays.asList(BigDecimal.ZERO));
        answer = "5";
        assertTrue(mut.get());
    }

    @Test
    void andArgumentsListHasNullAsFirstArgumentIsValidShouldBeTrue(){
        sut = new Between(Arrays.asList(null, BigDecimal.TEN));
        answer = "5";
        assertTrue(mut.get());
    }

    @Test
    void andArgumentsListHasNullAsSecondArgumentIsValidShouldBeTrue(){
        sut = new Between(Arrays.asList(BigDecimal.ZERO, null));
        answer = "5";
        assertTrue(mut.get());
    }

    @Test
    void andArgumentsListHasFirstArgumentGreaterThanSecondArgumentIsValidShouldBeFalse(){
        sut = new Between(Arrays.asList(BigDecimal.TEN, BigDecimal.ZERO));
        answer = "5";
        assertFalse(mut.get());
    }
}