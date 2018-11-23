package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestion;
import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestionOption;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class QuestionnaireQuestionView implements ViewModel<QuestionnaireQuestion> {

    public static final ViewModelFactory<QuestionnaireQuestion, QuestionnaireQuestionView> FACTORY = QuestionnaireQuestionView::new;

    public final QuestionnaireQuestionType type;
    public final String id;
    public final int ord;
    public final String name;
    public final String placeholder;
    public final List<ListQuestionnaireQuestionOption> options;
    public final String description;
    public final String classes;
    public final Boolean readOnly;
    public final Boolean locked;
    public final Boolean req;
    public final Map<String, Object> validations;


    public QuestionnaireQuestionView(QuestionnaireQuestion question) {
        type = question.getType();
        id = question.getId();
        ord = question.getOrd();
        name = question.getName();
        placeholder = question.getPlaceholder();
        options = getOptions(question);
        description = question.getDescription();
        classes = question.getClasses();
        readOnly = question.isReadOnly();
        locked = question.isLocked();
        req = question.isRequired();
        validations = question.mapValidations();
    }

    private List<ListQuestionnaireQuestionOption> getOptions(QuestionnaireQuestion question) {
        if(question instanceof ListQuestionnaireQuestion){
            return  ((ListQuestionnaireQuestion) question).getOptions();
        } else {
            return null;
        }
    }
}
