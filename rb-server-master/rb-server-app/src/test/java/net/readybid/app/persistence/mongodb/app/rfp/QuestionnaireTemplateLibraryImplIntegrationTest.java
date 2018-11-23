package net.readybid.app.persistence.mongodb.app.rfp;

import net.readybid.app.MongoDbHelperService;
import net.readybid.app.MongoIntegrationTestConfiguration;
import net.readybid.app.core.entities.rfp.*;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireGroup;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTable;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRow;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRowCell;
import net.readybid.app.persistence.mongodb.repository.implementation.QuestionnaireTemplateRepositoryImpl;
import net.readybid.test_utils.RbMapAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[ INTEGRATION ] QuestionnaireTemplateLibraryImpl")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {QuestionnaireTemplateLibraryImpl.class, QuestionnaireTemplateRepositoryImpl.class,
        MongoIntegrationTestConfiguration.class, MongoDbHelperService.class})
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@ActiveProfiles("default,integrationTest")
class QuestionnaireTemplateLibraryImplIntegrationTest {

    private static final List<String> modulesIds = Arrays.asList("PB", "CS");
    private static final List<String> sectionIds = Arrays.asList("PLC", "BFDT", "RTG");

    @Autowired
    private QuestionnaireTemplateLibraryImpl sut;

    @Autowired
    private MongoDbHelperService mongo;

    private QuestionnaireForm result;


    @BeforeEach
    void setup() throws Exception{
        mongo.drop();
        mongo.load("QuestionnaireTemplateLibraryImplTest-HotelQuestionnaire.json");
    }

    @AfterEach
    void teardown(){
        mongo.drop();
    }

    @Test
    void test(){
        result = sut.getTemplate();

        isTemplateHavingCorrectId();
        ModulesAssert.isLoadedCorrectly(result);
        SectionsAssert.isLoadedCorrectly(result);
//        isTemplateHavingCorrectSections();
//        isTemplateHavingCorrectQuestions();
//        isTemplateHavingCorrectTextQuestion();
//        isTemplateHavingCorrectListQuestion();
//        isTemplateHavingCorrectNumberQuestion();
//        isTemplateHavingCorrectDecimalQuestion();
//        isTemplateHavingCorrectDateQuestion();
//        isTemplateHavingCorrectUserDefinedQuestion();
    }

    private void isTemplateHavingCorrectId() {
        assertEquals("HOTEL", result.getId());
    }

    private static class ModulesAssert {
        private static void isLoadedCorrectly(QuestionnaireForm result) {
            final List<QuestionnaireModule> modules = result.getModules();
            areAllModulesLoaded(modules);
            isModuleLoadedCorrectly(modules);
        }

        private static void areAllModulesLoaded(List<QuestionnaireModule> modules) {
            for(int i = 0; i < modulesIds.size(); i++){
                assertEquals(modulesIds.get(i), modules.get(i).getId());
            }
        }

        private static void isModuleLoadedCorrectly(List<QuestionnaireModule> modules) {
            final QuestionnaireModule module = modules.get(0);
            assertEquals(modulesIds.get(0), module.getId());
            assertEquals("Property Basic", module.getName());
            assertEquals(1, module.getOrd());
            assertFalse(module.getSections().isEmpty());
        }
    }

    private static class SectionsAssert {

        private static void isLoadedCorrectly(QuestionnaireForm result) {
            final List<QuestionnaireSection> sections = result.getModules().get(0).getSections();
            areAllSectionsLoaded(sections);
            GroupSectionAssert.isLoadedCorrectly(sections);
            TableSectionAssert.isLoadedCorrectly(sections);
//        isRateGridSectionLoadedCorrectly(sections); todo
        }

        private static void areAllSectionsLoaded(List<QuestionnaireSection> sections) {
            for(int i = 0; i < sectionIds.size(); i++){
                assertEquals(sectionIds.get(i), sections.get(i).getId());
            }
        }
    }

    private static class GroupSectionAssert{

        private static void isLoadedCorrectly(List<QuestionnaireSection> sections) {
            final int sectionIndexForGroup = 0;
            assertTrue(sections.get(sectionIndexForGroup) instanceof QuestionnaireGroup);
            final QuestionnaireGroup group = (QuestionnaireGroup) sections.get(sectionIndexForGroup);

            assertEquals(sectionIds.get(sectionIndexForGroup), group.getId());
            assertEquals("Property Location / Contact", group.getName());
            assertEquals(1, group.getOrd());
            assertEquals(QuestionnaireSectionType.GROUP, group.getType());
            assertEquals("test", group.getClasses());
            assertFalse(group.getQuestions().isEmpty());
        }
    }

    private static class TableSectionAssert {

        private static void isLoadedCorrectly(List<QuestionnaireSection> sections) {
            final int sectionIndexForTable = 1;
            assertTrue(sections.get(sectionIndexForTable) instanceof QuestionnaireTable);
            final QuestionnaireTable table = (QuestionnaireTable) sections.get(sectionIndexForTable);

            assertEquals(sectionIds.get(sectionIndexForTable), table.getId());
            assertEquals("Blackout/Fair Date Rates", table.getName());
            assertEquals(2, table.getOrd());
            assertEquals(QuestionnaireSectionType.TABLE, table.getType());
            assertEquals("center-80", table.getClasses());
            areTableFiltersLoadedCorrectly(table.getFilters());
            areTableDefaultFiltersLoadedCorrectly(table.getDefaultFilters());
            areTableManageRowsLoadedCorrectly(table.getManageRows());
            areTableActionsLoadedCorrectly(table.getActions());
            areTableRowsLoadedCorrectly(table.getRows());
        }


        private static void areTableFiltersLoadedCorrectly(Map<String, Object> filters) {
            assertTrue(filters.keySet().contains("blackoutDate"));
            assertTrue(((List) filters.get("blackoutDate")).contains(1));
        }

        private static void areTableDefaultFiltersLoadedCorrectly(Map<String, Object> filters) {
            assertTrue(filters.keySet().contains("blackoutDate"));
            assertTrue(((List) filters.get("blackoutDate")).contains(1));
        }

        private static void areTableManageRowsLoadedCorrectly(List<Object> manageRows) {
            //noinspection unchecked
            Map<Object, Object> manageRow = (Map) manageRows.get(0);
            RbMapAssert.that(manageRow)
                    .containsExactly(
                            new AbstractMap.SimpleEntry<>("id", "blackoutDate"),
                            new AbstractMap.SimpleEntry<>("label", "Blackout Date")
                    );
        }

        private static void areTableActionsLoadedCorrectly(Map<String, Object> actions) {
            assertTrue(actions.containsKey("blackoutDate"));
            isTableActionLoadedCorrectly(actions.get("blackoutDate"));
        }

        private static void isTableActionLoadedCorrectly(Object action) {
            //noinspection unchecked
            final Map<Object, Object> actionMap = (Map) action;
            RbMapAssert.that(actionMap)
                    .contains(
                            new AbstractMap.SimpleEntry<>("id", "blackoutDate"),
                            new AbstractMap.SimpleEntry<>("label", "How many Blackout Date Periods will you allow?"),
                            new AbstractMap.SimpleEntry<>("type", "filter")
                    );
            areTableActionOptionsLoadedCorrectly(actionMap.get("options"));
        }

        private static void areTableActionOptionsLoadedCorrectly(Object options) {
            //noinspection unchecked
            final List<Map<Object, Object>> optionsList = (List) options;
            final Map<Object, Object> option = optionsList.get(0);
            RbMapAssert.that(option)
                    .contains(
                            new AbstractMap.SimpleEntry<>("label", "One Period"),
                            new AbstractMap.SimpleEntry<>("filter", Collections.singletonList(1)),
                            new AbstractMap.SimpleEntry<>("default", 1)
                    );
        }

        private static void areTableRowsLoadedCorrectly(List<QuestionnaireTableRow> rows) {
            areAllTableRowsLoaded(rows);
            isTableRowLoadedCorrectly(rows);
        }


        private static void areAllTableRowsLoaded(List<QuestionnaireTableRow> rows) {
            assertEquals(2, rows.size());
        }

        private static void isTableRowLoadedCorrectly(List<QuestionnaireTableRow> rows) {
            final QuestionnaireTableRow row = rows.get(0);

            assertEquals("ROW", row.type);
            assertEquals("row-header", row.classes);
            assertTrue(row.forFilters.containsKey("blackoutDate"));
            assertEquals(1, row.forFilters.get("blackoutDate"));
            areTableRowCellsLoadedCorrectly(row.cells);
        }

        private static void areTableRowCellsLoadedCorrectly(List<QuestionnaireTableRowCell> cells) {
            areAllTableRowCellsLoaded(cells);
            isTableRowCellLoadedCorrectly(cells);
        }

        private static void areAllTableRowCellsLoaded(List<QuestionnaireTableRowCell> cells) {
            assertEquals(2, cells.size());
        }

        private static void isTableRowCellLoadedCorrectly(List<QuestionnaireTableRowCell> cells) {
            final QuestionnaireTableRowCell cell = cells.get(0);
            assertEquals("test", cell.id);
            assertEquals("Blackout/Fair Date Period", cell.name);
            assertEquals("Blackout/Fair Date", cell.description);
            assertTrue(cell.variable);
            assertEquals(new Integer(2), cell.colSpan);
            assertEquals("occupancy", cell.colSpanId);
            assertEquals(new Integer(3), cell.rowSpan);
            assertEquals("roomType", cell.rowSpanId);
            assertEquals("text-center", cell.classes);
            assertTrue(cell.forFilters.containsKey("occupancy"));
            assertEquals("SGL", cell.forFilters.get("occupancy"));
            isCellQuestionCorrectlyLoaded(cell.question);
        }

        private static void isCellQuestionCorrectlyLoaded(QuestionnaireQuestion question) {
            assertEquals("BD1_START", question.getId());
            assertEquals(new Integer(326), question.getOrd());
            assertEquals("Start", question.getName());
            assertEquals(QuestionnaireQuestionType.DATE, question.getType());
        }
    }
}