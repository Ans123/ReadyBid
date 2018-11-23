package net.readybid.app.entities.rfp.questionnaire.form.section;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@DisplayName("Questionnaire Group Impl")
class QuestionnaireGroupTest {

    private QuestionnaireGroup sut;

    @Nested
    @DisplayName("Merge")
    class MergeTest{

        private QuestionnaireSection sectionToMerge;
        private List<QuestionnaireConfigurationItem> config;

        private Supplier<QuestionnaireSection> mut = () -> sut.merge(sectionToMerge, config);

        private QuestionnaireQuestion templateQuestion_1;
        private QuestionnaireQuestion templateQuestion_2;
        private QuestionnaireQuestion templateQuestion_3;

        private String id;
        private String name;
        private String classes;
        private int ord;

        @BeforeEach
        void setup(){
            id = "id_" + RbRandom.idAsString();
            name = "test_" + RbRandom.name();
            ord = 101;
            classes = "test_" + RbRandom.alphanumeric(50);

            templateQuestion_1 = mock(QuestionnaireQuestion.class);
            templateQuestion_2 = mock(QuestionnaireQuestion.class);
            templateQuestion_3 = mock(QuestionnaireQuestion.class);

            sut = new QuestionnaireGroup.Builder()
                    .setId(id)
                    .setName(name)
                    .setOrd(ord)
                    .setClasses(classes)
                    .setQuestions(Arrays.asList(templateQuestion_1, templateQuestion_2, templateQuestion_3)).build();
        }

        @Test
        @DisplayName("should merge model to template")
        void shouldMergeModelToTemplate(){
            config = new ArrayList<>();
            sectionToMerge = mock(QuestionnaireGroup.class);
            doReturn(id).when(sectionToMerge).getId();
            doReturn(RbRandom.name()).when(sectionToMerge).getName();
            doReturn(RbRandom.randomInt(100)).when(sectionToMerge).getOrd();

            final QuestionnaireQuestion modelQuestion_1 = mock(QuestionnaireQuestion.class);
            final QuestionnaireQuestion modelQuestion_2 = mock(QuestionnaireQuestion.class);

            doReturn(Arrays.asList(modelQuestion_1, modelQuestion_2)).when(sectionToMerge).getQuestions();

            final QuestionnaireQuestion resultQuestion_1 = mock(QuestionnaireQuestion.class);
            final QuestionnaireQuestion resultQuestion_2 = mock(QuestionnaireQuestion.class);

            doReturn(resultQuestion_1).when(templateQuestion_1).merge(same(modelQuestion_1));
            doReturn(resultQuestion_2).when(templateQuestion_3).merge(same(modelQuestion_2));

            final QuestionnaireSection result = mut.get();

            verify(templateQuestion_1, times(1)).merge(any(QuestionnaireQuestion.class));
            verify(templateQuestion_2, times(1)).merge(any(QuestionnaireQuestion.class));
            verify(templateQuestion_3, times(1)).merge(any(QuestionnaireQuestion.class));

            assertEquals(id, result.getId());
            assertEquals(name, result.getName());
            assertEquals(classes, result.getClasses());
            assertEquals(ord, result.getOrd());

            assertEquals(2, result.getQuestions().size());
            assertTrue(result.getQuestions().contains(resultQuestion_1));
            assertTrue(result.getQuestions().contains(resultQuestion_2));
        }
    }
}