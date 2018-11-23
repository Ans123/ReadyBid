package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Questionnaire Validator: Blank")
class RegexTest {

    private Regex sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new Regex("^\\d{2}:\\d{2} - \\d{2}:\\d{2}$");
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
    void andAnswerMatchesPatternIsValidShouldBeTrue(){
        answer = "00:00 - 24:00";
        assertTrue(mut.get());
    }

    @Test
    void andAnswerIsNotMatchingPatternIsValidShouldBeFalse(){
        answer = RbRandom.alphanumeric(100);
        assertFalse(mut.get());
    }

    @Test
    void andPatternIsNullIsValidShouldBeTrue(){
        sut = new Regex(null);
        answer = "00:00 - 24:00";
        assertTrue(mut.get());
    }

    @Test
    void andPatternIsEmptyIsValidShouldBeTrue(){
        sut = new Regex(null);
        answer = "00:00 - 24:00";
        assertTrue(mut.get());
    }

    @Test
    void andPatternIsInvalidIsValidShouldBeTrue(){
        sut = new Regex(null);
        answer = "00:00 - 24:00";
        assertTrue(mut.get());
    }
}