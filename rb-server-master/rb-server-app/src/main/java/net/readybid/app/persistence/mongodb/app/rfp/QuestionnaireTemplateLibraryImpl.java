package net.readybid.app.persistence.mongodb.app.rfp;

import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.interactors.rfp.gate.QuestionnaireTemplateLibrary;
import net.readybid.app.persistence.mongodb.repository.QuestionnaireTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class QuestionnaireTemplateLibraryImpl implements QuestionnaireTemplateLibrary {

    private static final String HOTEL_QUESTIONNAIRE_TEMPLATE_ID = "5b3a34c7538513afb98ee3cb";

    private final QuestionnaireTemplateRepository repository;
    private QuestionnaireForm hotelRfpQuestionnaireTemplate;

    @Autowired
    public QuestionnaireTemplateLibraryImpl(QuestionnaireTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuestionnaireForm getTemplate() {
        if(hotelRfpQuestionnaireTemplate == null){
            hotelRfpQuestionnaireTemplate = repository.findOne(byId(HOTEL_QUESTIONNAIRE_TEMPLATE_ID));
        }
        return hotelRfpQuestionnaireTemplate;
    }
}
