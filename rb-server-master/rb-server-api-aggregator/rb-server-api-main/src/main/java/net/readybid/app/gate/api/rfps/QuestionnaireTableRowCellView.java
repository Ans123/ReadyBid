package net.readybid.app.gate.api.rfps;

import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireTableRowCell;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.HashMap;

public class QuestionnaireTableRowCellView extends HashMap<String, Object> implements ViewModel<QuestionnaireTableRowCell> {

    public static final ViewModelFactory<QuestionnaireTableRowCell, QuestionnaireTableRowCellView> FACTORY = QuestionnaireTableRowCellView::new;

    public QuestionnaireTableRowCellView(QuestionnaireTableRowCell cell) {
        super();
        put("id", cell.id);
        put("name", cell.name);
        put("description", cell.description);
        put("variable", cell.variable);
        put("colspan", cell.colSpan);
        put("colspanId", cell.colSpanId);
        put("rowspan", cell.rowSpan);
        put("rowspanId", cell.rowSpanId);
        put("ckasses", cell.classes);
        put("for", cell.forFilters);
        put("cell", QuestionnaireQuestionView.FACTORY.createAsPartial(cell.question));
    }
}
