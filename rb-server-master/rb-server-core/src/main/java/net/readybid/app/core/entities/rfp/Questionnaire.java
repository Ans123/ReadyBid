package net.readybid.app.core.entities.rfp;

import net.readybid.rfp.type.RfpType;

import java.util.List;
import java.util.Map;

public interface Questionnaire {
    RfpType getType();

    QuestionnaireForm getModel();

    List<QuestionnaireConfigurationItem> getConfig();

    Map<String,String> getGlobals();

    QuestionnaireResponse getResponse();

    QuestionnaireResponse getResponseDraft();

    QuestionnaireConfigurationItem getConfigState(String configId);

    void setGlobals(Map<String, String> questionnaireGlobals);

    void prepareResponseDraft();

    void setResponse(QuestionnaireResponse response);

    Map<String,String> getAnswers();

    String getResponseAnswer(String id);

    List<String> getAcceptedRates();

    boolean containsAcceptedRates();
}
