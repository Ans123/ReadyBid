package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.entities.rfp.questionnaire.form.validation.QuestionnaireQuestionValidator;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@DisplayName("User Defined Questionnaire Question")
class UserDefinedQuestionnaireQuestionTest {

    private UserDefinedQuestionnaireQuestion sut;

    private QuestionnaireQuestionType type;
    private String id;
    private String name;
    private int ord;
    private String placeholder;
    private String description;
    private QuestionnaireQuestionValidator validator;

    @BeforeEach
    void setup(){
        type = QuestionnaireQuestionType.USER_DEFINED;
        id = RbRandom.idAsString();
        name = RbRandom.name();
        ord = RbRandom.randomInt(100);
        placeholder = RbRandom.alphanumeric(100);
        description = RbRandom.alphanumeric(100);
        validator = mock(QuestionnaireQuestionValidator.class);

        sut = (UserDefinedQuestionnaireQuestion) new QuestionnaireQuestionBuilder()
                .setType(type)
                .setId(id)
                .setName(name)
                .setOrd(ord)
                .setDescription(description)
                .setPlaceholder(placeholder)
                .setValidator(validator)
                .build();
    }

    @Nested
    @DisplayName("Merge")
    class MergeTest {

        private QuestionnaireQuestion questionModel;

        private Supplier<QuestionnaireQuestion> mut = () -> sut.merge(questionModel);

        @Test
        @DisplayName("should merge model with template")
        void test(){
            final QuestionnaireQuestionValidator validator = mock(QuestionnaireQuestionValidator.class);
            final List<ListQuestionnaireQuestionOption> options = Arrays.asList(
                    mock(ListQuestionnaireQuestionOption.class), mock(ListQuestionnaireQuestionOption.class));

            questionModel = new QuestionnaireQuestionBuilder()
                    .setType(QuestionnaireQuestionType.LIST)
                    .setId(id)
                    .setName(RbRandom.name())
                    .setClasses(RbRandom.alphanumeric(100))
                    .setOrd(ord)
                    .setDescription(RbRandom.alphanumeric(100))
                    .setPlaceholder(RbRandom.alphanumeric(100))
                    .setRequired(RbRandom.bool())
                    .setValidator(validator)
                    .setOptions(options)
                    .build();

            final QuestionnaireQuestion result = mut.get();

            assertTrue(result instanceof ListQuestionnaireQuestion);

            final ListQuestionnaireQuestion resultAsImpl = (ListQuestionnaireQuestion) result;
            QuestionnaireQuestionImplAssert.that(resultAsImpl.question)
                    .hasId(questionModel.getId())
                    .hasName(questionModel.getName())
                    .hasClasses(questionModel.getClasses())
                    .hasOrd(questionModel.getOrd())
                    .hasPlaceholder(questionModel.getPlaceholder())
                    .hasDescription(questionModel.getDescription())
                    .hasReadOnly(null)
                    .hasLocked(null)
                    .hasRequired(questionModel.isRequired())
                    .hasValidator(validator);
            assertTrue(resultAsImpl.options.containsAll(options));
        }


        @Test
        @DisplayName("when model is null it should be null")
        void shouldBeNullWhenModelIsNull(){
            questionModel = null;
            final QuestionnaireQuestion result = mut.get();
            assertNull(result);
        }
    }
}