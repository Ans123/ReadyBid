package net.readybid.app.entities.rfp.questionnaire.form;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireModule;
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

@DisplayName("Questionnaire Module Impl")
class QuestionnaireModuleImplTest {

    private QuestionnaireModuleImpl sut;

    @Nested
    @DisplayName("Merge")
    class MergeTest{

        private QuestionnaireModule moduleToMerge;
        private List<QuestionnaireConfigurationItem> config;

        private Supplier<QuestionnaireModule> mut = () -> sut.merge(moduleToMerge, config);

        private QuestionnaireSection templateSection_1;
        private QuestionnaireSection templateSection_2;
        private QuestionnaireSection templateSection_3;

        private String id;
        private String name;
        private int ord;

        @BeforeEach
        void setup(){
            id = "id_" + RbRandom.idAsString();
            name = "test_" + RbRandom.name();
            ord = 101;

            templateSection_1 = mock(QuestionnaireSection.class);
            templateSection_2 = mock(QuestionnaireSection.class);
            templateSection_3 = mock(QuestionnaireSection.class);

            sut = new QuestionnaireModuleImpl.Builder()
                    .setId(id)
                    .setName(name)
                    .setOrd(ord)
                    .setSections(Arrays.asList(templateSection_1, templateSection_2, templateSection_3)).build();
        }

        @Test
        @DisplayName("should merge model to template")
        void shouldMergeModelToTemplate(){
            moduleToMerge = mock(QuestionnaireModule.class);
            config = new ArrayList<>();
            doReturn(id).when(moduleToMerge).getId();
            doReturn(RbRandom.name()).when(moduleToMerge).getName();
            doReturn(RbRandom.randomInt(100)).when(moduleToMerge).getOrd();

            final QuestionnaireSection modelSection_1 = mock(QuestionnaireSection.class);
            final QuestionnaireSection modelSection_2 = mock(QuestionnaireSection.class);

            doReturn(Arrays.asList(modelSection_1, modelSection_2)).when(moduleToMerge).getSections();

            final QuestionnaireSection resultSection_1 = mock(QuestionnaireSection.class);
            final QuestionnaireSection resultSection_2 = mock(QuestionnaireSection.class);

            doReturn(resultSection_1).when(templateSection_1).merge(same(modelSection_1), same(config));
            doReturn(resultSection_2).when(templateSection_3).merge(same(modelSection_2), same(config));

            final QuestionnaireModule result = mut.get();

            verify(templateSection_1, times(1)).merge(any(QuestionnaireSection.class), same(config));
            verify(templateSection_2, times(1)).merge(any(QuestionnaireSection.class), same(config));
            verify(templateSection_3, times(1)).merge(any(QuestionnaireSection.class), same(config));

            assertEquals(id, result.getId());
            assertEquals(name, result.getName());
            assertEquals(ord, result.getOrd());

            assertEquals(2, result.getSections().size());
            assertTrue(result.getSections().contains(resultSection_1));
            assertTrue(result.getSections().contains(resultSection_2));
        }
    }
}