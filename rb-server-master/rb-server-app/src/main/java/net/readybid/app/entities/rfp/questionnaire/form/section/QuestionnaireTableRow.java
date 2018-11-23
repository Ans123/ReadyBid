package net.readybid.app.entities.rfp.questionnaire.form.section;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.entities.IBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;

public class QuestionnaireTableRow {

    public final String type;
    public final String classes;
    public final Map<String, Object> forFilters;
    public final List<QuestionnaireTableRowCell> cells;

    private QuestionnaireTableRow(Builder builder) {
        type = builder.type;
        classes = builder.classes;
        forFilters = builder.forFilter == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.forFilter);
        cells = builder.cells == null ? EMPTY_LIST : Collections.unmodifiableList(builder.cells);
    }

    QuestionnaireTableRow applyConfig(QuestionnaireConfigurationItem config) {
        if(!config.includes(forFilters)) return null;
        return new Builder()
                .setType(type)
                .setClasses(classes)
                .setForFilters(forFilters)
                .setCells(applyConfig(cells, config))
                .build();
    }

    private List<QuestionnaireTableRowCell> applyConfig(List<QuestionnaireTableRowCell> cells, QuestionnaireConfigurationItem config) {
        final List<QuestionnaireTableRowCell> filteredCells = new ArrayList<>();
        for(QuestionnaireTableRowCell cell : cells){
            final QuestionnaireTableRowCell filteredCell = cell.applyConfig(config);
            if(filteredCell != null) filteredCells.add(filteredCell);
        }
        return filteredCells;
    }

    public List<QuestionnaireQuestion> getQuestions() {
        return cells.stream()
                .map(QuestionnaireTableRowCell::getQuestion)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static class Builder implements IBuilder<QuestionnaireTableRow> {

        private String type;
        private String classes;
        private Map<String, Object> forFilter;
        private List<QuestionnaireTableRowCell> cells;

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setClasses(String classes) {
            this.classes = classes;
            return this;
        }

        public Builder setForFilters(Map<String, Object> forFilter) {
            this.forFilter = forFilter;
            return this;
        }

        public Builder setCells(List<QuestionnaireTableRowCell> cells) {
            this.cells = cells;
            return this;
        }

        public QuestionnaireTableRow build(){
            return new QuestionnaireTableRow(this);
        }
    }
}
