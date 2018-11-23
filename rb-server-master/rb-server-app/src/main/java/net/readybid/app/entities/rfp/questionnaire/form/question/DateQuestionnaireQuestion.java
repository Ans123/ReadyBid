package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DateQuestionnaireQuestion implements QuestionnaireQuestion {

    private final static List<String> DATE_PATTERNS_TO_CHECK = Arrays.asList("M/d/yy", "d.M.yy", "M/d/yyyy", "d.M.yyyy", "M-d-yyyy", "d/M/yyyy", "d-M-yyyy", "yyyy/M/d");

    final QuestionnaireQuestionImpl question;

    DateQuestionnaireQuestion(QuestionnaireQuestionImpl question) {
        this.question = question;
    }

    @Override
    public QuestionnaireQuestion merge(QuestionnaireQuestion model) {
        return model == null ? null : new DateQuestionnaireQuestion(question.merge(model));
    }

    @Override
    public Boolean isValidAnswer(Map<String, String> answers, Map<String, String> context) {
        return question.isValidAnswer(answers, context);
    }

    @Override
    public String getError(Map<String, String> answers, Map<String, String> context) {
        return question.getError(answers, context);
    }

    @Override
    public QuestionnaireQuestionType getType() {
        return question.getType();
    }

    @Override
    public Boolean isReadOnly() {
        return question.isReadOnly();
    }

    @Override
    public Boolean isLocked() {
        return question.isLocked();
    }

    @Override
    public Map<String, Object> mapValidations() {
        return question.mapValidations();
    }

    @Override
    public String getId() {
        return question.getId();
    }

    @Override
    public String getName() {
        return question.getName();
    }

    @Override
    public String getPlaceholder() {
        return question.getPlaceholder();
    }

    @Override
    public String getClasses() {
        return question.getClasses();
    }

    @Override
    public Integer getOrd() {
        return question.getOrd();
    }

    @Override
    public String getDescription() {
        return question.getDescription();
    }

    @Override
    public Boolean isRequired() {
        return question.isRequired();
    }

    @Override
    public String getAnswer(Map<String, String> answers, Map<String, String> responseContext) {
        return question.getAnswer(answers, responseContext);
    }

    @Override
    public void parseAnswer(Map<String, String> answers) {
        question.parseAnswer(answers);
        final String answer = answers.get(getId());
        if(!(answer == null || answer.isEmpty())) {
            LocalDate date = defaultParse(answer);
            if(date == null) date = customParse(answer);
            if(date != null) {
                answers.put(getId(), date.toString());
            }
        }
    }

    private LocalDate customParse(String answer) {
        for (String datePattern : DATE_PATTERNS_TO_CHECK) {
            try {
                return  LocalDate.parse(answer, DateTimeFormatter.ofPattern(datePattern));
            } catch (DateTimeParseException ignore) {}
        }
        return null;
    }

    private LocalDate defaultParse(String answer) {
        try {
            return LocalDate.parse(answer);
        } catch (DateTimeParseException dtpe){
            return null;
        }
    }
}
