package net.readybid.app.core.entities.rfp;

import java.util.*;

public class QuestionnaireResponseImpl implements QuestionnaireResponse {

    protected Map<String, String> answers;
    protected List<QuestionnaireConfigurationItem> state;
    protected boolean isValid = false;
    protected long errorsCount = 0L;
    protected List<String> errorsList;
    protected boolean touched;

    public QuestionnaireResponseImpl() {}

    public QuestionnaireResponseImpl(QuestionnaireResponse responseModel) {
        this.answers = new LinkedHashMap<>(responseModel.getAnswers());
        this.state = new ArrayList<>(responseModel.getState());
        this.isValid = responseModel.isValid();
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public Map<String, String> getAnswers() {
        return answers;
    }

    @Override
    public void setAnswer(String key, String value) {
        answers.put(key, value);
    }

    @Override
    public QuestionnaireConfigurationItem getStateItem(String stateItemId) {
        return this.state.stream().filter(s -> s.id.equalsIgnoreCase(stateItemId)).findFirst().orElse(null);
    }

    @Override
    public String getAnswer(String id) {
        return answers == null ? null : answers.get(id);
    }

    public List<QuestionnaireConfigurationItem> getState() {
        return state;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public void setState(List<QuestionnaireConfigurationItem> state) {
        this.state = state;
    }

    public void setErrorsCount(long errorsCount) {
        this.errorsCount = errorsCount;
    }

    public long getErrorsCount() {
        return errorsCount;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public void touch() {
        this.touched = true;
    }

    public void setErrorsList(List<String> errors) {
        errorsList = errors;
    }

    public List<String> getErrorsList() {
        return errorsList;
    }
}
