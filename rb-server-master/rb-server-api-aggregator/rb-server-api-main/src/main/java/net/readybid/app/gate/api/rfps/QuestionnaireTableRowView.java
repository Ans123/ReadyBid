package net.readybid.app.gate.api.rfps;

import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRow;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.HashMap;

public class QuestionnaireTableRowView extends HashMap<String, Object> implements ViewModel<QuestionnaireTableRow> {

    public static final ViewModelFactory<QuestionnaireTableRow, QuestionnaireTableRowView> FACTORY = QuestionnaireTableRowView::new;

    public QuestionnaireTableRowView(QuestionnaireTableRow row) {
        super();
        put("type", row.type);
        put("classes", row.classes);
        put("for", row.forFilters);
        put("cells", QuestionnaireTableRowCellView.FACTORY.createList(row.cells));
    }
}
