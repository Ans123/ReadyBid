package net.readybid.app.entities.rfp.questionnaire;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.rfp.type.RfpType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionnaireImpl implements Questionnaire {

    private RfpType type;
    private QuestionnaireForm model;
    private List<QuestionnaireConfigurationItem> config;
    private Map<String, String> globals = new HashMap<>();
    private HotelRfpAcceptedRates accepted;
    private QuestionnaireResponse response;
    private QuestionnaireResponse responseDraft;

    public void setType(RfpType type) {
        this.type = type;
    }

    public void setModel(QuestionnaireForm model) {
        this.model = model;
    }

    public void setConfig(List<QuestionnaireConfigurationItem> config) {
        this.config = config;
    }

    @Override
    public RfpType getType() {
        return type;
    }

    @Override
    public QuestionnaireForm getModel() {
        return model;
    }

    @Override
    public List<QuestionnaireConfigurationItem> getConfig() {
        return config;
    }

    @Override
    public Map<String, String> getGlobals() {
        return globals;
    }

    @Override
    public QuestionnaireResponse getResponse() {
        return response;
    }

    @Override
    public QuestionnaireResponse getResponseDraft() {
        return responseDraft;
    }

    @Override
    public QuestionnaireConfigurationItem getConfigState(String configId) {
        final QuestionnaireConfigurationItem configItem = getConfigItem(configId);
        final QuestionnaireConfigurationItem stateItem = getStateItem(configId);
        final QuestionnaireConfigurationItem item = new QuestionnaireConfigurationItem();

        item.id = configId;
        item.data = new HashMap<>();
        if(configItem != null) item.data.putAll(configItem.data);
        if(stateItem != null) item.data.putAll(stateItem.data);

        return item;
    }

    private QuestionnaireConfigurationItem getConfigItem(String configId) {
        return this.config.stream().filter(i -> i.id.equalsIgnoreCase(configId)).findFirst().orElse(null);
    }

    private QuestionnaireConfigurationItem getStateItem(String id) {
        return response == null ? null : response.getStateItem(id);
    }

    public void setGlobals(Map<String, String> globals) {
        this.globals = globals;
    }

    @Override
    public void prepareResponseDraft() {
        this.responseDraft = this.response;
    }

    public void setResponse(QuestionnaireResponse response) {
        this.response = response;
    }

    @Override
    public Map<String, String> getAnswers() {
        return response == null ? null : response.getAnswers();
    }

    @Override
    public String getResponseAnswer(String id) {
        return null == response ? null : response.getAnswer(id);
    }

    @Override
    public List<String> getAcceptedRates() {
        return accepted == null ? null : accepted.acceptedRates;
    }

    @Override
    public boolean containsAcceptedRates() {
        return accepted != null && accepted.containsRates();
    }

    public HotelRfpAcceptedRates getAccepted() {
        return accepted;
    }

    public void setResponseDraft(QuestionnaireResponse responseDraft) {
        this.responseDraft = responseDraft;
    }

    public void setAccepted(HotelRfpAcceptedRates acceptedRates) {
        this.accepted = acceptedRates;
    }
}
