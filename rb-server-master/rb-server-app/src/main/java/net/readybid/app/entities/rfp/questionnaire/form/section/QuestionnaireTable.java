package net.readybid.app.entities.rfp.questionnaire.form.section;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.IBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;

public class QuestionnaireTable implements QuestionnaireSection {

    private final QuestionnaireSectionType type;
    private final String id;
    private final String name;
    private final int ord;
    private final String classes;
    private final Map<String, Object> filters;
    private final Map<String, Object> defaultFilters;
    private final List<Object> manageRows;
    private final Map<String, Object> actions;
    private final List<QuestionnaireTableRow> rows;

    private QuestionnaireTable(Builder builder) {
        type = builder.type;
        id = builder.id;
        name = builder.name;
        ord = builder.ord;
        classes = builder.classes;
        filters = builder.filters == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.filters);
        defaultFilters = builder.defaultFilters == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.defaultFilters);
        manageRows = builder.manageRows == null ? EMPTY_LIST : Collections.unmodifiableList(builder.manageRows);
        actions = builder.actions == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.actions);
        rows = builder.rows == null ? EMPTY_LIST : Collections.unmodifiableList(builder.rows);
    }

    @Override
    public QuestionnaireSection merge(QuestionnaireSection model, List<QuestionnaireConfigurationItem> configList) {
        final QuestionnaireConfigurationItem config = configList.stream().filter( c -> Objects.equals(c.id, id)).findFirst().orElse(null);
        if(config == null) return null;

        return new Builder()
                .setType(type)
                .setId(id)
                .setName(name)
                .setOrd(ord)
                .setClasses(classes)
                .setFilters(filters)
                .setDefaultFilters(defaultFilters)
                .setManageRows(manageRows)
                .setActions(actions)
                .setRows(applyConfig(config))
                .build();
    }

    @Override
    public QuestionnaireSectionType getType() {
        return type;
    }

    private List<QuestionnaireTableRow> applyConfig(QuestionnaireConfigurationItem config) {
        final List<QuestionnaireTableRow> filteredRows = new ArrayList<>();
        for(QuestionnaireTableRow row : rows){
            final QuestionnaireTableRow filteredRow = row.applyConfig(config);
            if(filteredRow != null) filteredRows.add(filteredRow);
        }
        return filteredRows;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public int getOrd() {
        return ord;
    }

    @Override
    public List<QuestionnaireQuestion> getQuestions() {
        return rows.stream()
                .map(QuestionnaireTableRow::getQuestions)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public Map<String, Object> getDefaultFilters() {
        return defaultFilters;
    }

    public List<Object> getManageRows() {
        return manageRows;
    }

    public Map<String, Object> getActions() {
        return actions;
    }

    public List<QuestionnaireTableRow> getRows() {
        return rows;
    }

    public static class Builder implements IBuilder<QuestionnaireTable>{

        private QuestionnaireSectionType type;
        private String id;
        private String name;
        private int ord;
        private String classes;
        private Map<String, Object> filters;
        private Map<String, Object> defaultFilters;
        private List<Object> manageRows;
        private Map<String, Object> actions;
        private List<QuestionnaireTableRow> rows;

        public Builder setType(QuestionnaireSectionType type) {
            this.type = type;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setOrd(int ord) {
            this.ord = ord;
            return this;
        }

        public Builder setClasses(String classes) {
            this.classes = classes;
            return this;
        }

        public Builder setFilters(Map<String, Object> filters) {
            this.filters = filters;
            return this;
        }

        public Builder setDefaultFilters(Map<String, Object> defaultFilters) {
            this.defaultFilters = defaultFilters;
            return this;
        }

        public Builder setManageRows(List<Object> manageRows) {
            this.manageRows = manageRows;
            return this;
        }

        public Builder setActions(Map<String, Object> actions) {
            this.actions = actions;
            return this;
        }

        public Builder setRows(List<QuestionnaireTableRow> rows) {
            this.rows = rows;
            return this;
        }

        @Override
        public QuestionnaireTable build(){
            return new QuestionnaireTable(this);
        }
    }
}
