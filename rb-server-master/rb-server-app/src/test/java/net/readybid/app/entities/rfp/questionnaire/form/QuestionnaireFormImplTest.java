package net.readybid.app.entities.rfp.questionnaire.form;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.core.entities.rfp.QuestionnaireModule;
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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@DisplayName("Questionnaire Form Impl")
class QuestionnaireFormImplTest {

    private QuestionnaireFormImpl sut;

    @Nested
    @DisplayName("Merge")
    class MergeTest{

        private Questionnaire questionnaireToMerge;

        private Supplier<QuestionnaireForm> mut = () -> sut.merge(questionnaireToMerge);

        private QuestionnaireModule templateModule_1;
        private QuestionnaireModule templateModule_2;
        private QuestionnaireModule templateModule_3;

        @BeforeEach
        void setup(){
            templateModule_1 = mock(QuestionnaireModule.class);
            templateModule_2 = mock(QuestionnaireModule.class);
            templateModule_3 = mock(QuestionnaireModule.class);

            sut = new QuestionnaireFormImpl.Builder()
                    .setModules(Arrays.asList(templateModule_1, templateModule_2, templateModule_3)).build();
        }

        @Test
        void test(){
            questionnaireToMerge = mock(Questionnaire.class);
            final QuestionnaireForm modelForm = mock(QuestionnaireForm.class);
            final List<QuestionnaireConfigurationItem> config = new ArrayList<>();
            doReturn(modelForm).when(questionnaireToMerge).getModel();
            doReturn(config).when(questionnaireToMerge).getConfig();

            final QuestionnaireModule modelModule_1 = mock(QuestionnaireModule.class);
            final QuestionnaireModule modelModule_2 = mock(QuestionnaireModule.class);

            doReturn(Arrays.asList(modelModule_1, modelModule_2)).when(modelForm).getModules();

            final QuestionnaireModule resultModule_1 = mock(QuestionnaireModule.class);
            final QuestionnaireModule resultModule_2 = mock(QuestionnaireModule.class);

            doReturn(resultModule_1).when(templateModule_1).merge(same(modelModule_1), same(config));
            doReturn(resultModule_2).when(templateModule_3).merge(same(modelModule_2), same(config));

            final QuestionnaireForm result = mut.get();

            verify(templateModule_1, times(1)).merge(any(QuestionnaireModule.class), same(config));
            verify(templateModule_2, times(1)).merge(any(QuestionnaireModule.class), same(config));
            verify(templateModule_3, times(1)).merge(any(QuestionnaireModule.class), same(config));

            assertEquals(2, result.getModules().size());
            assertTrue(result.getModules().contains(resultModule_1));
            assertTrue(result.getModules().contains(resultModule_2));
        }
    }
}