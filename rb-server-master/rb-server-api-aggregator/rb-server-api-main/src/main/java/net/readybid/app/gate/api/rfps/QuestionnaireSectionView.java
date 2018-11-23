package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

public abstract class QuestionnaireSectionView implements ViewModel<QuestionnaireSection> {

    public static final ViewModelFactory<QuestionnaireSection, QuestionnaireSectionView> FACTORY = QuestionnaireSectionView::CreateView;

    private static QuestionnaireSectionView CreateView(QuestionnaireSection section) {
        QuestionnaireSectionView view = null;
        QuestionnaireSectionType sectionType = section.getType();
        if(sectionType == null) sectionType = QuestionnaireSectionType.GROUP;
        switch (sectionType){
            case TABLE:
                view = QuestionnaireTableView.FACTORY.createAsPartial(section);
                break;
            case HOTEL_RFP_RATE_GRID:
                view = QuestionnaireHotelRfpRateGridView.FACTORY.createAsPartial(section);
                break;
            case GROUP:
            default:
                view = QuestionnaireGroupView.FACTORY.createAsPartial(section);
                break;
        }
        return view;
    }
}
