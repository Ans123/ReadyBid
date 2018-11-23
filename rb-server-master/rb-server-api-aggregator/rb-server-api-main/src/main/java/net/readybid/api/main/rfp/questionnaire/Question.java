package net.readybid.api.main.rfp.questionnaire;

import net.readybid.api.main.rfp.questionnaire.parsers.QuestionParser;
import net.readybid.api.main.rfp.questionnaire.parsers.QuestionnaireAnswerParseException;
import net.readybid.api.main.rfp.questionnaire.validators.QuestionValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class Question {

    private final String id;
    private final int ordinalNumber;

    private final ArrayList<QuestionParser> parsers = new ArrayList<>();
    private final ArrayList<QuestionValidator> validators = new ArrayList<>();

    public Question(int ordinalNumber, String id) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
    }

    public Question pushParser(QuestionParser parser){
        parsers.add(parser);
        return this;
    }

    public Question pushValidator(QuestionValidator validator){
        validators.add(validator);
        return this;
    }

    public Object parse(Object value) throws QuestionnaireAnswerParseException {
        Object parsedValue = value;
        for(QuestionParser parser: parsers){
            try {
                parsedValue = parser.parse(value);
            } catch (Exception e){
                throw new QuestionnaireAnswerParseException(parser.getParseErrorCode());
            }
        }
        return parsedValue;
    }

    public List<String> validate(Object value){
        List<String> errors = new ArrayList<>();
        for(QuestionValidator validator : validators){
            try {
                if(validator.isNotValid(value)) {
                    errors.add(validator.getErrorCode());
                }
            } catch (Exception e) {
                errors.add(validator.getErrorCode());
            }
        }
        return errors;
    }

    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    public String getId() {
        return id;
    }
}
