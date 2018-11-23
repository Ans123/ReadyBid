package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireHotelRfpRateGrid;
import net.readybid.web.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class QuestionnaireHotelRfpRateGridView extends QuestionnaireSectionView {

    public static final ViewModelFactory<QuestionnaireSection, QuestionnaireHotelRfpRateGridView> FACTORY = QuestionnaireHotelRfpRateGridView::new;

    public final QuestionnaireSectionType type;
    public final String id;
    public final Integer ord;
    public final boolean locked;
    public final Map<String, Object> filters;
    public final Map<String, Object> defaultFilters;
    public final Map<String, Object> actions;
    public final List<QuestionnaireSectionView> cells;

    public QuestionnaireHotelRfpRateGridView(QuestionnaireSection section) {
        if(section instanceof QuestionnaireHotelRfpRateGrid){
            final QuestionnaireHotelRfpRateGrid rateGrid = (QuestionnaireHotelRfpRateGrid) section;

            type = rateGrid.getType();
            id = rateGrid.getId();
            ord = rateGrid.getOrd();
            locked = rateGrid.getLocked();
            filters = rateGrid.getFilters();
            defaultFilters = rateGrid.getDefaultFilters();
            actions = rateGrid.getActions();
            cells = QuestionnaireSectionView.FACTORY.createList(rateGrid.getSections());
        } else {
            type = null;
            id = null;
            ord =  null;
            locked = false;
            filters = null;
            defaultFilters = null;
            actions = null;
            cells =  null;
        }
    }
}
