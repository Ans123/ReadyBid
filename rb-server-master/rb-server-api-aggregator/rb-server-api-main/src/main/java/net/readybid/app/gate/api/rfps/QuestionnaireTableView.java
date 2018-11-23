package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTable;
import net.readybid.web.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class QuestionnaireTableView extends QuestionnaireSectionView {

    public static final ViewModelFactory<QuestionnaireSection, QuestionnaireTableView> FACTORY = QuestionnaireTableView::new;

    public final QuestionnaireSectionType type;
    public final String id;
    public final String name;
    public final Integer ord;
    public final String classes;
    public final Map<String, Object> filters;
    public final Map<String, Object> defaultFilters;
    public final List<Object> manageRows;
    public final Map<String, Object> actions;
    public final List<QuestionnaireTableRowView> cells;

    public QuestionnaireTableView(QuestionnaireSection section) {
        if(section instanceof QuestionnaireTable){
            final QuestionnaireTable table = (QuestionnaireTable) section;

            type = table.getType();
            id = table.getId();
            name = table.getName();
            ord = table.getOrd();
            classes = table.getClasses();
            filters = table.getFilters();
            defaultFilters = table.getDefaultFilters();
            manageRows = table.getManageRows();
            actions = table.getActions();
            cells = QuestionnaireTableRowView.FACTORY.createList(table.getRows());

        } else {

            type = null;
            id = null;
            name = null;
            ord =  null;
            classes =  null;
            filters = null;
            defaultFilters = null;
            manageRows = null;
            actions = null;
            cells =  null;
        }

    }
}
