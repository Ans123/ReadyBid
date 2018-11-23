package net.readybid.app.entities.rfp.questionnaire.form.section;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.entities.IBuilder;

import java.util.Collections;
import java.util.Map;

import static java.util.Collections.EMPTY_MAP;

public class QuestionnaireTableRowCell {

    public final String id;
    public final String name;
    public final String description;
    public final boolean variable;
    public final Integer colSpan;
    public final String colSpanId;
    public final Integer rowSpan;
    public final String rowSpanId;
    public final String classes;
    public final Map<String, Object> forFilters; // for in template
    public final QuestionnaireQuestion question; // cell in template

    private QuestionnaireTableRowCell(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        variable = builder.variable;
        colSpan = builder.colSpan;
        colSpanId = builder.colSpanId;
        rowSpan = builder.rowSpan;
        rowSpanId = builder.rowSpanId;
        classes = builder.classes;
        forFilters = builder.forFilters == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.forFilters);
        question = builder.question;
    }

    QuestionnaireTableRowCell applyConfig(QuestionnaireConfigurationItem config) {
        if(!config.includes(forFilters)) return null;
        return new Builder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setVariable(variable)
                .setColSpan(colSpan)
                .setColSpanId(colSpanId)
                .setRowSpan(rowSpan)
                .setRowSpanId(rowSpanId)
                .setClasses(classes)
                .setForFilters(forFilters)
                .setQuestion(question)
                .build();
    }

    QuestionnaireQuestion getQuestion() {
        return question;
    }

    public static class Builder implements IBuilder<QuestionnaireTableRowCell> {

        private String name;
        private Integer colSpan;
        private String colSpanId;
        private Integer rowSpan;
        private String rowSpanId;
        private String classes;
        private Map<String, Object> forFilters;
        private QuestionnaireQuestion question;
        private String id;
        private String description;
        private boolean variable;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setColSpan(Integer colSpan) {
            this.colSpan = colSpan;
            return this;
        }

        public Builder setColSpanId(String colSpanId) {
            this.colSpanId = colSpanId;
            return this;
        }

        public Builder setRowSpan(Integer rowSpan) {
            this.rowSpan = rowSpan;
            return this;
        }

        public Builder setRowSpanId(String rowSpanId) {
            this.rowSpanId = rowSpanId;
            return this;
        }

        public Builder setClasses(String classes) {
            this.classes = classes;
            return this;
        }

        public Builder setForFilters(Map<String, Object> forFilters) {
            this.forFilters = forFilters;
            return this;
        }

        public Builder setQuestion(QuestionnaireQuestion question) {
            this.question = question;
            return this;
        }

        public QuestionnaireTableRowCell build(){
            return new QuestionnaireTableRowCell(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setVariable(boolean variable) {
            this.variable = variable;
            return this;
        }
    }
}
