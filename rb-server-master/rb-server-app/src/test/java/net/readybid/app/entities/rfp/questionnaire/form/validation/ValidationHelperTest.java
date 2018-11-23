package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ValidationHelper")
class ValidationHelperTest {

    @Nested
    @DisplayName("Get Value For Field")
    class GetValueForFieldTest{

        private String field;
        private Map<String, String> answers;
        private Map<String, String> context;

        private Supplier<String> mut = () -> ValidationHelper.getValueForField(field, answers, context);

        @BeforeEach
        void setup(){
            answers = new HashMap<>();
            answers.put(RbRandom.alphanumeric(10) + "_1", RbRandom.alphanumeric(50));
            answers.put(RbRandom.alphanumeric(10) + "_2", RbRandom.alphanumeric(50));
            context = new HashMap<>(answers);
        }

        @Test
        @DisplayName("when field has Hash prefix return value from answers")
        void whenFieldHasHashPrefixReturnValueFromAnswers(){
            final String key = RbRandom.alphanumeric(10);
            final String expectedValue = RbRandom.alphanumeric(20);
            answers.put(key, expectedValue);
            context.put(key, expectedValue + "_1");
            field = "#" + key;
            assertSame(expectedValue, mut.get());
        }

        @Test
        @DisplayName("when field has Hash prefix And answers don't contain field return empty String")
        void whenFieldHasHashPrefixAndAnswersDoNotContainFieldReturnEmptyString(){
            final String key = RbRandom.alphanumeric(10);
            final String expectedValue = RbRandom.alphanumeric(20);
            context.put(key, expectedValue + "_1");
            field = "#" + key;
            assertTrue(mut.get().isEmpty());
        }

        @Test
        @DisplayName("when field has Dollar sign prefix return value from context")
        void whenFieldHasDollarPrefixReturnValueFromContext(){
            final String key = RbRandom.alphanumeric(10);
            final String expectedValue = RbRandom.alphanumeric(20);
            answers.put(key, expectedValue + "_1");
            context.put(key, expectedValue);
            field = "$" + key;
            assertSame(expectedValue, mut.get());
        }

        @Test
        @DisplayName("when field has Dollar Sign prefix And context don't contain field return empty String")
        void whenFieldHasDollarSignPrefixAndContextDoNotContainFieldReturnEmptyString(){
            final String key = RbRandom.alphanumeric(10);
            final String expectedValue = RbRandom.alphanumeric(20);
            answers.put(key, expectedValue + "_1");
            field = "$" + key;
            assertTrue(mut.get().isEmpty());
        }

        @Test
        @DisplayName("when field doesn't have prefix return field as value")
        void whenFieldDoesNotHavePrefixReturnFieldAsValue(){
            field = "no_prefix" + RbRandom.alphanumeric(10);
            assertSame(field, mut.get());
        }
    }
}