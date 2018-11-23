package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireGroup;
import net.readybid.web.ViewModelFactory;

import java.util.List;

public class QuestionnaireGroupView extends QuestionnaireSectionView {

    public static final ViewModelFactory<QuestionnaireSection, QuestionnaireGroupView> FACTORY = QuestionnaireGroupView::new;

    public final QuestionnaireSectionType type;
    public final String id;
    public final String name;
    public final Integer ord;
    public final String classes;
    public final List<QuestionnaireQuestionView> cells;

    public QuestionnaireGroupView(QuestionnaireSection section) {
        if(section instanceof QuestionnaireGroup){
            final QuestionnaireGroup group = (QuestionnaireGroup) section;

            type = group.getType();
            id = group.getId();
            name = group.getName();
            ord = group.getOrd();
            classes = group.getClasses();
            cells = QuestionnaireQuestionView.FACTORY.createList(group.getQuestions());
        } else {
            type = null;
            id = null;
            name = null;
            ord =  null;
            classes =  null;
            cells =  null;
        }
    }
}
