package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireModule;
import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;

public class QuestionnaireModuleView implements ViewModel<QuestionnaireModule> {

    public static final ViewModelFactory<QuestionnaireModule, QuestionnaireModuleView> FACTORY = QuestionnaireModuleView::new;

    public final String id;
    public final String name;
    public final int ord;
    public final List<QuestionnaireSectionView> cells;

    public QuestionnaireModuleView(QuestionnaireModule module) {
        id = module.getId();
        name = module.getName();
        ord = module.getOrd();
        cells =  QuestionnaireSectionView.FACTORY.createList(module.getSections());
    }
}
