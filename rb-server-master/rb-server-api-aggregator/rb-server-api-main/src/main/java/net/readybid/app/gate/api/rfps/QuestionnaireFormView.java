package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;

public class QuestionnaireFormView implements ViewModel<QuestionnaireForm> {

    public static final ViewModelFactory<QuestionnaireForm, QuestionnaireFormView> FACTORY = QuestionnaireFormView::new;

    public final String id;
    public final List<QuestionnaireModuleView> cells;

    public QuestionnaireFormView(QuestionnaireForm form) {
        this.id = form.getId();
        this.cells = QuestionnaireModuleView.FACTORY.createList(form.getModules());
    }
}
