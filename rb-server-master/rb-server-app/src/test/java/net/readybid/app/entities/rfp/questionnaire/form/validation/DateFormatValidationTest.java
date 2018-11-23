package net.readybid.app.entities.rfp.questionnaire.form.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Questionnaire Validator: Date Format Validation")
class DateFormatValidationTest {

    private DateFormatValidation sut;

    private String answer;

    private Map<String, String> answers;

    private Map<String, String> context;

    private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

    @BeforeEach
    void setup(){
        sut = new DateFormatValidation("YYYY-MM-DD");
    }

    @Test
    void andAnswerIsInISO8601FormatIsValidShouldBeTrue(){
        answer = "2018-01-01";
        assertTrue(mut.get());
    }

    @Test
    void andAnswerIsNotInRequiredFormatIsValidShouldBeFalse(){
        answer = "2018-31-12";
        assertFalse(mut.get());
    }

    @Test
    void andAnswerIsNotInRequiredFormatIsValidShouldBeFalse_2(){
        answer = "12/31/16";
        assertFalse(mut.get());
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
    void andFormatIsNullIsValidShouldBeTrue(){
        sut = new DateFormatValidation(null);
        answer = "2018-01-01";
        assertTrue(mut.get());
    }

    @Test
    void andFormatIsEmptyIsValidShouldBeTrue(){
        sut = new DateFormatValidation("");
        answer = "2018-01-01";
        assertTrue(mut.get());
    }

    @Test
    void andFormatIsNotISOIsValidShouldBeTrue(){
        sut = new DateFormatValidation("YYYY/MM/DD");
        answer = "2018-01-01";
        assertTrue(mut.get());
    }
}