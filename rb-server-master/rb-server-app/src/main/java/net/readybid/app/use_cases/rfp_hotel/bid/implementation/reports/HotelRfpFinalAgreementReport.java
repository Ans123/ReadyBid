package net.readybid.app.use_cases.rfp_hotel.bid.implementation.reports;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireHotelRfpRateGrid.RATE_PATTERN;

public class HotelRfpFinalAgreementReport {

    public final Map<String, String> responses;
    public final List<? extends RateLoadingInformation> rateLoadingInformation;
    public final String buyerCompanyName;
    public final String buyerCompanyAccountId;

    HotelRfpFinalAgreementReport(QuestionnaireForm template, HotelRfpBid hotelRfpBid) {
        final Map<String, String> acceptedResponses = getAcceptedResponses(template, hotelRfpBid.getQuestionnaire());
        responses = acceptedResponses == null ? null : Collections.unmodifiableMap(acceptedResponses);
        final List<? extends RateLoadingInformation> rateLoadingInformation = hotelRfpBid.getRateLoadingInformation();
        this.rateLoadingInformation = rateLoadingInformation == null ? null : Collections.unmodifiableList(rateLoadingInformation);
        buyerCompanyName = hotelRfpBid.getBuyerCompanyName();
        buyerCompanyAccountId = String.valueOf(hotelRfpBid.getBuyerCompanyAccountId());
    }

    private Map<String, String> getAcceptedResponses(QuestionnaireForm template, Questionnaire questionnaire) {
        final Map<String, String> responses = new HashMap<>();
        final QuestionnaireForm form = template.merge(questionnaire);
        final List<QuestionnaireQuestion> questions = form.getQuestions();
        final List<String> acceptedRates = questionnaire.getAcceptedRates();

        for(QuestionnaireQuestion question : questions){
            final String questionId = question.getId();
            final String answer = getAnswer(questionnaire, acceptedRates, questionId);
            if(answer != null) {
                responses.put(questionId, answer);
            }
        }
        return responses;
    }

    private static String getAnswer(Questionnaire questionnaire, List<String> acceptedRates, String questionId) {
        if(isAcceptedByBuyer(questionId, acceptedRates)){
            return questionnaire.getResponseAnswer(questionId);
        } else {
            return null;
        }
    }

    private static boolean isAcceptedByBuyer(String questionId, List<String> acceptedRates) {
        return acceptedRates == null || !RATE_PATTERN.matcher(questionId).matches() || acceptedRates.contains(questionId);
    }
}
