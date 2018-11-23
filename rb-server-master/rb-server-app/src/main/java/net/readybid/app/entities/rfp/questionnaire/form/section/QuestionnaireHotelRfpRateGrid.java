package net.readybid.app.entities.rfp.questionnaire.form.section;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.IBuilder;
import net.readybid.app.entities.rfp.questionnaire.form.question.QuestionnaireQuestionBuilder;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;

public class QuestionnaireHotelRfpRateGrid implements QuestionnaireSection {

    public static final Pattern RATE_PATTERN = Pattern.compile("^(LRA|NLRA|GOVT)_S[1-5]_RT[1-3]_(SGL|DBL)$");

    private final QuestionnaireSectionType type;
    private final String id;
    private final int ord;
    private final boolean locked;
    private final Map<String, Object> filters;
    private final Map<String, Object> defaultFilters;
    private final Map<String, Object> actions;
    private final List<QuestionnaireSection> sections;

    private QuestionnaireHotelRfpRateGrid(Builder builder) {
        type = builder.type;
        id = builder.id;
        ord = builder.ord;
        locked = builder.locked;
        filters = builder.filters == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.filters);
        defaultFilters = builder.defaultFilters == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.defaultFilters);
        actions = builder.actions == null ? EMPTY_MAP : Collections.unmodifiableMap(builder.actions);
        sections = builder.sections == null ? EMPTY_LIST : Collections.unmodifiableList(builder.sections);
    }

    @Override
    public QuestionnaireSection merge(QuestionnaireSection model, List<QuestionnaireConfigurationItem> configList) {
        final QuestionnaireConfigurationItem config = configList.stream().filter( c -> Objects.equals(c.id, "RT")).findFirst().orElse(null);
        if(config == null) return null;

        return new Builder()
                .setType(type)
                .setId(id)
                .setOrd(ord)
                .setLocked(locked)
                .setFilters(filters)
                .setDefaultFilters(defaultFilters)
                .setActions(actions)
                .setSections(applyConfig(model, config, configList))
                .build();
    }

    @Override
    public QuestionnaireSectionType getType() {
        return type;
    }

    private List<QuestionnaireSection> applyConfig(QuestionnaireSection model, QuestionnaireConfigurationItem config, List<QuestionnaireConfigurationItem> configList) {
        final List<QuestionnaireSection> mergedSections = new ArrayList<>();
        for(QuestionnaireSection section : sections){
            QuestionnaireSection mergedSection = null;
            if(section instanceof QuestionnaireGroup){
                mergedSection = section.merge(createModelFromConfig(config), configList);
            } else if(section instanceof QuestionnaireTable){
                mergedSection = section.merge(model, configList);
            }
            if(mergedSection != null) mergedSections.add(mergedSection);
        }
        return mergedSections;
    }

    private QuestionnaireSection createModelFromConfig(QuestionnaireConfigurationItem config) {
        final List roomTypeConfig = (List)config.data.get("roomType");
        final List<QuestionnaireQuestion> roomTypeQuestions = generateRoomTypeQuestions(roomTypeConfig);
        return new QuestionnaireGroup.Builder()
                .setId("RTD")
                .setQuestions(roomTypeQuestions)
                .build();
    }

    private List<QuestionnaireQuestion> generateRoomTypeQuestions(List config) {
        final String roomTypeDefineTemplate = "ROOMTYPE%sDEFINE";
        final String roomTypeNumberTemplate = "ROOMTYPE%sNUMBER";
        final List<QuestionnaireQuestion> roomTypeQuestions = new ArrayList<>();
        for(Object o : config){
            roomTypeQuestions.add(new QuestionnaireQuestionBuilder().setId(String.format(roomTypeDefineTemplate, o)).build());
            roomTypeQuestions.add(new QuestionnaireQuestionBuilder().setId(String.format(roomTypeNumberTemplate, o)).build());
        }
        return roomTypeQuestions;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getClasses() {
        return "";
    }

    @Override
    public int getOrd() {
        return ord;
    }

    @Override
    public List<QuestionnaireQuestion> getQuestions() {
        return sections.stream()
                .map(QuestionnaireSection::getQuestions)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public boolean getLocked() {
        return locked;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public Map<String, Object> getDefaultFilters() {
        return defaultFilters;
    }

    public Map<String, Object> getActions() {
        return actions;
    }

    public List<QuestionnaireSection> getSections() {
        return sections;
    }

    public static class Builder implements IBuilder<QuestionnaireHotelRfpRateGrid> {

        private QuestionnaireSectionType type;
        private String id;
        private int ord;
        private boolean locked;
        private Map<String, Object> filters;
        private Map<String, Object> defaultFilters;
        private Map<String, Object> actions;
        private List<QuestionnaireSection> sections;

        @Override
        public QuestionnaireHotelRfpRateGrid build() {
            return new QuestionnaireHotelRfpRateGrid(this);
        }

        public Builder setType(QuestionnaireSectionType type) {
            this.type = type;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setOrd(int ord) {
            this.ord = ord;
            return this;
        }

        public Builder setLocked(boolean locked) {
            this.locked = locked;
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

        public Builder setActions(Map<String, Object> actions) {
            this.actions = actions;
            return this;
        }

        public Builder setSections(List<QuestionnaireSection> sections) {
            this.sections = sections;
            return this;
        }
    }
}
