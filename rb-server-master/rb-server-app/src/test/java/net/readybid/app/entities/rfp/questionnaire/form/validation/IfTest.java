package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@DisplayName("If ")
class IfTest {

    @Nested
    @DisplayName("Is Valid")
    class IsValidTest{

        private String field;
        private If.Condition condition;
        private List<QuestionnaireQuestionValidation> thenValidations;
        private List<QuestionnaireQuestionValidation> elseValidations;

        private QuestionnaireQuestionValidation then_1;
        private QuestionnaireQuestionValidation then_2;
        private QuestionnaireQuestionValidation else_1;
        private QuestionnaireQuestionValidation else_2;

        private String answer;
        private Map<String, String> answers;
        private Map<String, String> context;

        private If sut;
        private Supplier<Boolean> mut = () -> sut.isValid(answer, answers, context);

        @BeforeEach
        void setup(){
            then_1 = mock(QuestionnaireQuestionValidation.class);
            then_2 = mock(QuestionnaireQuestionValidation.class);
            else_1 = mock(QuestionnaireQuestionValidation.class);
            else_2 = mock(QuestionnaireQuestionValidation.class);

            field = RbRandom.alphanumeric(20);
            condition = mock(If.Condition.class);
            thenValidations = Arrays.asList(then_1, then_2);
            elseValidations = Arrays.asList(else_1, else_2);

            answer = RbRandom.alphanumeric(20);
            answers = new HashMap<>();
            context = new HashMap<>();

            sut = new If(field, condition, thenValidations, elseValidations);
        }

        @Nested
        @DisplayName("when condition is true")
        class WhenConditionIsTrue{

            @BeforeEach
            void setup(){
                doReturn(true).when(condition).isTrue(same(field));
                doReturn(true).when(else_1).isValid(same(answer), same(answers), same(context));
                doReturn(true).when(else_2).isValid(same(answer), same(answers), same(context));
            }

            @Test
            @DisplayName("should test all Then Validations")
            void shouldTestAllThenValidations(){
                doReturn(true).when(then_1).isValid(same(answer), same(answers), same(context));
                doReturn(true).when(then_2).isValid(same(answer), same(answers), same(context));

                mut.get();

                verify(then_1, times(1)).isValid(same(answer), same(answers), same(context));
                verify(then_2, times(1)).isValid(same(answer), same(answers), same(context));
                verify(else_1, times(0)).isValid(same(answer), same(answers), same(context));
                verify(else_2, times(0)).isValid(same(answer), same(answers), same(context));
            }

            @Test
            @DisplayName("should be True if all Then Validations pass")
            void shouldBeTrueIfAllThenValidationsPass(){
                doReturn(true).when(then_1).isValid(same(answer), same(answers), same(context));
                doReturn(true).when(then_2).isValid(same(answer), same(answers), same(context));

                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be False if any Then Validations fails")
            void shouldBeFalseIfAnyThenValidationsPass(){
                doReturn(true).when(then_1).isValid(same(answer), same(answers), same(context));
                doReturn(false).when(then_2).isValid(same(answer), same(answers), same(context));

                assertFalse(mut.get());
            }
        }

        @Nested
        @DisplayName("when condition is false")
        class WhenConditionIsFalse{

            @BeforeEach
            void setup(){
                doReturn(false).when(condition).isTrue(same(field));
                doReturn(true).when(then_1).isValid(same(answer), same(answers), same(context));
                doReturn(true).when(then_2).isValid(same(answer), same(answers), same(context));
            }

            @Test
            @DisplayName("should test all Else Validations")
            void shouldTestAllElseValidations(){
                doReturn(true).when(else_1).isValid(same(answer), same(answers), same(context));
                doReturn(true).when(else_2).isValid(same(answer), same(answers), same(context));

                mut.get();

                verify(then_1, times(0)).isValid(same(answer), same(answers), same(context));
                verify(then_2, times(0)).isValid(same(answer), same(answers), same(context));
                verify(else_1, times(1)).isValid(same(answer), same(answers), same(context));
                verify(else_2, times(1)).isValid(same(answer), same(answers), same(context));
            }

            @Test
            @DisplayName("should be True if all Else Validations pass")
            void shouldBeTrueIfAllElseValidationsPass(){
                doReturn(true).when(else_1).isValid(same(answer), same(answers), same(context));
                doReturn(true).when(else_2).isValid(same(answer), same(answers), same(context));

                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be True if there are no Else Validations")
            void shouldBeTrueIfThereAreNoElseValidations(){
                sut = new If(field, condition, thenValidations, null);

                assertTrue(mut.get());

                sut = new If(field, condition, thenValidations, new ArrayList<>());

                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be False if any Else Validations fails")
            void shouldBeFalseIfAnyElseValidationsPass(){
                doReturn(true).when(else_1).isValid(same(answer), same(answers), same(context));
                doReturn(false).when(else_2).isValid(same(answer), same(answers), same(context));

                assertFalse(mut.get());
            }
        }
    }

    @Nested
    @DisplayName("Blank")
    class BlankTest{

        private If.Blank sut;
        private String value;

        private Supplier<Boolean> mut = () -> sut.isTrue(value);

        @Nested
        @DisplayName("is True")
        class IsTrueTest {

            @BeforeEach
            void setup(){
                sut = new If.Blank(true);
            }

            @Test
            @DisplayName("should be True for Null")
            void shouldBeTrueForNull(){
                value = null;
                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be True for Empty String")
            void shouldBeTrueForEmptyString(){
                value = "";
                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be True for String of Spaces")
            void shouldBeTrueForStringOfSpaces(){
                value = "   ";
                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be False for non-empty String")
            void shouldBeFalseForNonEmptyString(){
                value = RbRandom.alphanumeric(100, false);
                assertFalse(mut.get());
            }
        }

        @Nested
        @DisplayName("is False")
        class IsFalseTest {
            @BeforeEach
            void setup(){
                sut = new If.Blank(false);
            }

            @Test
            @DisplayName("should be False for Null")
            void shouldBeFalseForNull(){
                value = null;
                assertFalse(mut.get());
            }

            @Test
            @DisplayName("should be False for Empty String")
            void shouldBeFalseForEmptyString(){
                value = "";
                assertFalse(mut.get());
            }

            @Test
            @DisplayName("should be False for String of Spaces")
            void shouldBeFalseForStringOfSpaces(){
                value = "   ";
                assertFalse(mut.get());
            }

            @Test
            @DisplayName("should be True for non-empty String")
            void shouldBeTrueForNonEmptyString(){
                value = RbRandom.alphanumeric(100, false);
                assertTrue(mut.get());
            }
        }
    }

    @Nested
    @DisplayName("If Equal")
    class EqualTest{

        private If.Equal sut;
        private String valueToCompareWith;
        private String valueToCheck;

        private Supplier<Boolean> mut = () -> sut.isTrue(valueToCheck);

        @BeforeEach
        void setup(){
            valueToCompareWith = RbRandom.alphanumeric(100, false);
        }

        @Nested
        @DisplayName("is True")
        class IsTrueTest {

            @BeforeEach
            void setup(){
                sut = new If.Equal(valueToCompareWith, true);
            }

            @Test
            @DisplayName("should be False for Null")
            void shouldBeTrueForNull(){
                valueToCheck = null;
                assertFalse(mut.get());
            }

            @Test
            @DisplayName("should be True for Equal String")
            void shouldBeTrueForEqualString(){
                valueToCheck = valueToCompareWith;
                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be False for not equal String")
            void shouldBeFalseForNotEqualString(){
                valueToCheck = valueToCompareWith + RbRandom.alphanumeric(5, true);
                assertFalse(mut.get());
            }
        }

        @Nested
        @DisplayName("is False")
        class IsFalseTest {

            @BeforeEach
            void setup(){
                sut = new If.Equal(valueToCompareWith, false);
            }

            @Test
            @DisplayName("should be True for Null")
            void shouldBeTrueForNull(){
                valueToCheck = null;
                assertTrue(mut.get());
            }

            @Test
            @DisplayName("should be False for Equal String")
            void shouldBeFalseForEqualString(){
                valueToCheck = valueToCompareWith;
                assertFalse(mut.get());
            }

            @Test
            @DisplayName("should be True for not equal String")
            void shouldBeTrueForNotEqualString(){
                valueToCheck = valueToCompareWith + RbRandom.alphanumeric(5, true);
                assertTrue(mut.get());
            }
        }

        @Nested
        @DisplayName("with Null")
        class WithNullTest {

            @Nested
            @DisplayName("is True")
            class IsTrueTest {

                @BeforeEach
                void setup(){
                    sut = new If.Equal(null, true);
                }

                @Test
                @DisplayName("should be True for Null")
                void shouldBeTrueForNull(){
                    valueToCheck = null;
                    assertTrue(mut.get());
                }

                @Test
                @DisplayName("should be False for Other")
                void shouldBeFalseForOther(){
                    valueToCheck = RbRandom.alphanumeric(10, false);
                    assertFalse(mut.get());
                }
            }

            @Nested
            @DisplayName("is False")
            class IsFalseTest {

                @BeforeEach
                void setup(){
                    sut = new If.Equal(null, false);
                }

                @Test
                @DisplayName("should be False for Null")
                void shouldBeFalseForNull(){
                    valueToCheck = null;
                    assertFalse(mut.get());
                }

                @Test
                @DisplayName("should be True for Other")
                void shouldBeTrueForOther(){
                    valueToCheck = RbRandom.alphanumeric(5, false);
                    assertTrue(mut.get());
                }
            }
        }
    }

    @Nested
    @DisplayName("If Greater Then")
    class GreaterThenTest{

        private String valueToCompareWith;
        private String valueToCheck;

        private Supplier<Boolean> mut = () -> new If.GreaterThen(valueToCompareWith).isTrue(valueToCheck);

        @Test
        @DisplayName("should be True if Greater")
        void shouldBeTrueIfGreater(){
            valueToCompareWith = "0";
            valueToCheck = "1";

            assertTrue(mut.get());
        }

        @Test
        @DisplayName("should be False if Equal")
        void shouldBeFalseIfEqual(){
            valueToCompareWith = "0";
            valueToCheck = "0";

            assertFalse(mut.get());
        }

        @Test
        @DisplayName("should be False if Less")
        void shouldBeFalseIfLess(){
            valueToCompareWith = "1";
            valueToCheck = "0";

            assertFalse(mut.get());
        }

        @Test
        @DisplayName("should be False if Null")
        void shouldBeFalseIfLNull(){
            valueToCompareWith = "1";
            valueToCheck = null;

            assertFalse(mut.get());
        }

        @Test
        @DisplayName("should Throw if value to compare with is not a Number")
        void shouldThrowIfValueToCompareWithIsNotANumber(){
            valueToCompareWith = "abs";
            valueToCheck = "0";

            assertThrows(NumberFormatException.class, () -> mut.get());
        }

        @Test
        @DisplayName("should be False if value to check is not a Number")
        void shouldBeFalseIfValueToCheckIsNotANumber(){
            valueToCompareWith = "0";
            valueToCheck = "abs";

            assertFalse(mut.get());
        }
    }

    @Nested
    @DisplayName("If In")
    class InTest{

        private List<String> valuesToCompareWith;
        private String valueToCheck;

        private Supplier<Boolean> mut = () -> new If.In(valuesToCompareWith).isTrue(valueToCheck);

        @Test
        @DisplayName("should be True if in List")
        void shouldBeTrueIfInList(){
            valueToCheck = RbRandom.alphanumeric(50);
            valuesToCompareWith = Arrays.asList(RbRandom.name(), RbRandom.idAsString(), RbRandom.alphanumeric(5), String.valueOf(valueToCheck));

            assertTrue(mut.get());
        }

        @Test
        @DisplayName("should be False if not in List")
        void shouldBeFalseIfNotInList(){
            valueToCheck = RbRandom.alphanumeric(50);
            valuesToCompareWith = Arrays.asList(RbRandom.name(), RbRandom.idAsString(), RbRandom.alphanumeric(5));

            assertFalse(mut.get());
        }

        @Test
        @DisplayName("should be False if Null and Null not in List")
        void shouldBeFalseIfLess(){
            valueToCheck = null;
            valuesToCompareWith = Arrays.asList(RbRandom.name(), RbRandom.idAsString(), RbRandom.alphanumeric(5));

            assertFalse(mut.get());
        }

        @Test
        @DisplayName("should be True if Null and Null is in List")
        void shouldBeFalseIfLess1(){
            valueToCheck = null;
            valuesToCompareWith = Arrays.asList(RbRandom.name(), RbRandom.idAsString(), RbRandom.alphanumeric(5), null);

            assertTrue(mut.get());
        }
    }
}