package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.rfp.QuestionnaireResponseImpl;
import net.readybid.app.interactors.rfp.gate.QuestionnaireTemplateLibrary;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidQuestionnaireResponseProducer;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
class HotelRfpBidQuestionnaireResponseProducerImpl implements HotelRfpBidQuestionnaireResponseProducer {

    private final QuestionnaireTemplateLibrary questionnaireTemplateLibrary;
    private final HotelRfpBidResponseStateProducer stateProducer;
    private final HotelRfpBidResponseValidator validator;

    @Autowired
    HotelRfpBidQuestionnaireResponseProducerImpl(
            QuestionnaireTemplateLibrary questionnaireTemplateLibrary,
            HotelRfpBidResponseStateProducer stateProducer,
            HotelRfpBidResponseValidator validator
    ) {
        this.questionnaireTemplateLibrary = questionnaireTemplateLibrary;
        this.stateProducer = stateProducer;
        this.validator = validator;
    }

    @Override
    public QuestionnaireResponse prepareDraftResponse(HotelRfpBid bid) {
        return prepareResponse(bid.getResponseAnswers(), bid, true);
    }

    QuestionnaireResponse prepareDraftResponse(Map<String, String> answers, HotelRfpBid bid) {
        return prepareResponse(answers, bid, true);
    }

    QuestionnaireResponse prepareResponse(Map<String, String> answers, HotelRfpBid bid) {
        return prepareResponse(answers, bid, false);
    }

    private QuestionnaireResponse prepareResponse(Map<String, String> answers, HotelRfpBid bid, boolean isDraft){
        if(bid == null || answers == null || answers.isEmpty()) return null;
        final QuestionnaireForm template = questionnaireTemplateLibrary.getTemplate();
        final QuestionnaireForm form = template.merge(bid.getQuestionnaire());
        final List<String> errors = validator.parseAndValidate(form, answers, bid, isDraft);
        final List<QuestionnaireConfigurationItem> state = stateProducer.recreateState(answers);

        return createQuestionnaireResponse(answers, errors, state);
    }

    private static QuestionnaireResponse createQuestionnaireResponse(Map<String, String> answers, List<String> errors, List<QuestionnaireConfigurationItem> state) {
        final QuestionnaireResponseImpl response = new QuestionnaireResponseImpl();

        response.setErrorsList(errors);
        response.setIsValid(errors.isEmpty());
        response.setErrorsCount(errors.size());

        response.setAnswers(answers);
        response.setState(state);

        return response;
    }
}
