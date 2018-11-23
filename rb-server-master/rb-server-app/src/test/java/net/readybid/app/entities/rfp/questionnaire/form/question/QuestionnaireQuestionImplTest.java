package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.entities.rfp.questionnaire.form.validation.QuestionnaireQuestionValidator;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@DisplayName("Questionnaire Question Impl")
class QuestionnaireQuestionImplTest {

    private QuestionnaireQuestionImpl sut;

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

    @BeforeEach
    void setup(){
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

        sut = new QuestionnaireQuestionImpl(
                new QuestionnaireQuestionBuilder()
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
        );
    }

    @Nested
    @DisplayName("Merge")
    class MergeTest {

        private QuestionnaireQuestion questionModel;

        private Supplier<QuestionnaireQuestionImpl> mut = () -> sut.merge(questionModel);

        @BeforeEach
        void setup(){
        }

        @Test
        @DisplayName("should merge model with template")
        void test(){
            questionModel = new QuestionnaireQuestionImpl(
                    new QuestionnaireQuestionBuilder()
                            .setId(id)
                            .setOrd(RbRandom.randomInt(100))
                            .setRequired(true)
            );

            QuestionnaireQuestionImplAssert.that(mut.get())
                    .hasId(id)
                    .hasName(name)
                    .hasClasses(classes)
                    .hasOrd(ord)
                    .hasPlaceholder(placeholder)
                    .hasDescription(description)
                    .hasReadOnly(readOnly)
                    .hasLocked(locked)
                    .hasRequired(true)
                    .hasValidator(validator);
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