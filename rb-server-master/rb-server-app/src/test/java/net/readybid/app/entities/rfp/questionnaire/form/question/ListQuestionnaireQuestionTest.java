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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("List Questionnaire Question")
class ListQuestionnaireQuestionTest {

    private ListQuestionnaireQuestion sut;

    private QuestionnaireQuestionType type;
    private String id;
    private String name;
    private String classes;
    private int ord;
    private String placeholder;
    private String description;
    private boolean readOnly;
    private boolean locked;
    private boolean required;
    private QuestionnaireQuestionValidator validator;
    private List<ListQuestionnaireQuestionOption> options;

    @BeforeEach
    void setup(){
        type = QuestionnaireQuestionType.LIST;
        id = RbRandom.idAsString();
        name = RbRandom.name();
        classes = RbRandom.alphanumeric(100);
        ord = RbRandom.randomInt(100);
        placeholder = RbRandom.alphanumeric(100);
        description = RbRandom.alphanumeric(100);
        readOnly = RbRandom.bool();
        locked = RbRandom.bool();
        required = RbRandom.bool();
        validator = mock(QuestionnaireQuestionValidator.class);
        options = Arrays.asList(mock(ListQuestionnaireQuestionOption.class), mock(ListQuestionnaireQuestionOption.class));

        sut = (ListQuestionnaireQuestion) new QuestionnaireQuestionBuilder()
                .setType(type)
                .setId(id)
                .setName(name)
                .setClasses(classes)
                .setOrd(ord)
                .setPlaceholder(placeholder)
                .setDescription(description)
                .setReadOnly(readOnly)
                .setLocked(locked)
                .setRequired(required)
                .setValidator(validator)
                .setOptions(options)
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
            questionModel = new QuestionnaireQuestionBuilder()
                    .setId(id)
                    .setOrd(ord)
                    .setRequired(RbRandom.bool())
                    .build();

            final QuestionnaireQuestion result = mut.get();

            assertTrue(result instanceof ListQuestionnaireQuestion);

            final ListQuestionnaireQuestion resultAsImpl = (ListQuestionnaireQuestion) result;
            QuestionnaireQuestionImplAssert.that(resultAsImpl.question)
                    .hasId(id)
                    .hasName(name)
                    .hasClasses(classes)
                    .hasOrd(ord)
                    .hasPlaceholder(placeholder)
                    .hasDescription(description)
                    .hasReadOnly(readOnly)
                    .hasLocked(locked)
                    .hasRequired(required || questionModel.isRequired())
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