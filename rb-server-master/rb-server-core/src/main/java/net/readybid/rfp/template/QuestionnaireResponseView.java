package net.readybid.rfp.template;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class QuestionnaireResponseView implements ViewModel<QuestionnaireResponse> {

    public static final ViewModelFactory<QuestionnaireResponse, QuestionnaireResponseView> FACTORY = QuestionnaireResponseView::new;

    public Map<String, String> answers;
    public List<QuestionnaireConfigurationItem> state;
    public boolean isValid;
    public long errorsCount;
    public boolean touched;


    public QuestionnaireResponseView(QuestionnaireResponse questionnaireResponse) {
        answers = questionnaireResponse.getAnswers();
        state = questionnaireResponse.getState();
        isValid = questionnaireResponse.isValid();
        errorsCount = questionnaireResponse.getErrorsCount();
        touched = questionnaireResponse.isTouched();
    }
}
