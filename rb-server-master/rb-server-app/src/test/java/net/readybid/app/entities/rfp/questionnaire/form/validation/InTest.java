package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: In")
class InTest {

    private In sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    private String testAnswer = "test";

    @BeforeEach
    void setup(){
        sut = new In(EMPTY_LIST);
    }

    @Test
    void whenAnswerIsInListIsValidShouldBeTrue(){
        sut = new In(Arrays.asList(RbRandom.alphanumeric(10, true), testAnswer));
        answer = testAnswer;
        assertTrue(mut.get());
    }

    @Test
    void whenAnswerIsNotInTheListIsValidShouldBeFalse(){
        answer = testAnswer;
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
    void whenListIsNullIsValidShouldBeFalse(){
        sut = new In(null);
        answer = testAnswer;
        assertFalse(mut.get());
    }
}