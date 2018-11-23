package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.entities.IBuilder;

public class ListQuestionnaireQuestionOption {

    public final String value;
    public final String label;

    private ListQuestionnaireQuestionOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static class Builder implements IBuilder<ListQuestionnaireQuestionOption>{

        private String value;
        private String label;

        @Override
        public ListQuestionnaireQuestionOption build() {
            return new ListQuestionnaireQuestionOption(value, label);
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
