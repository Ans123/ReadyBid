package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.validation.*;
import net.readybid.exceptions.UnrecoverableException;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import org.bson.BsonReader;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


public class QuestionnaireQuestionValidatorCodec extends RbMongoCodecWithProvider<QuestionnaireQuestionValidator> {

    public QuestionnaireQuestionValidatorCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireQuestionValidator.class, bsonTypeClassMap);
    }

    @Override
    public QuestionnaireQuestionValidator decode(BsonReader reader, DecoderContext context) {
        final Document d = documentCodec().decode(reader, context);
        if(d == null || d.isEmpty()) return null;

        final List<QuestionnaireQuestionValidation> validations = new ArrayList<>();

        for(String key : d.keySet()){
            decodeValidation(d, key, validations);
        }
        return new QuestionnaireQuestionValidatorImpl(validations);
    }

    private void decodeValidation(Document d, String key, List<QuestionnaireQuestionValidation> validations) {
        QuestionnaireQuestionValidation v = null;
        switch (key.toLowerCase()){
            case "after":
                v = createDateValidation(d, key, After::new);
                break;
            case "before":
                v = createDateValidation(d, key, Before::new);
                break;
            case "between":
                v = new Between(readListOfBigDecimals(d, key));
                break;
            case "blank":
                v = new Blank(readBoolean(d.get(key)));
                break;
            case "date_format":
                v = new DateFormatValidation(d.getString(key));
                break;
            case "decimal":
                v = new Decimal(readLong(d, key));
                break;
            case "if":
                createIf(d.get(key), validations);
                break;
            case "in":
                v = new In(readListOfStrings(d, key));
                break;
            case "max_value":
                v = new MaxValue(readBigDecimal(d, key));
                break;
            case "min_value":
                v = new MinValue(readBigDecimal(d, key));
                break;
            case "max":
                v = new Max(readLong(d, key));
                break;
            case "min":
                v = new Min(readLong(d, key));
                break;
            case "numeric":
                v = new Numeric(readBoolean(d.get(key)));
                break;
            case "regex":
                v = new Regex(d.getString(key));
                break;
            case "required":
                v = new Required(readBoolean(d.get(key)));
                break;
            case "value":
                v = new Equal(d.getString(key), true);
                break;
            case "valuenot":
                v = new Equal(d.getString(key), false);
                break;
            default:
                System.err.println(String.format("Unknown Questionnaire Validation: %s", key));
                v = null;
        }
        if(v != null) validations.add(v);
    }

    private List<QuestionnaireQuestionValidation> decodeValidation(Document d) {
        if(d == null || d.isEmpty()) return null;
        final String key = d.keySet().stream().findFirst().orElse("");
        final List<QuestionnaireQuestionValidation> validations = new ArrayList<>();
        decodeValidation(d, key, validations);
        return validations;
    }

    private List<BigDecimal> readListOfBigDecimals(Document d, String key) {
        return readListOf(d, key, o -> new BigDecimal(String.valueOf(o)));
    }

    private List<String> readListOfStrings(Document d, String key) {
        return readListOf(d, key, String::valueOf);
    }

    private <T> List<T> readListOf(Document d, String key, Function<Object, T> converter) {
        final List<?> oList = (List) d.get(key);
        return oList.stream().map(converter).collect(Collectors.toList());
    }

    private If.Condition decodeCondition(List condition) {
        final String conditionOperator = String.valueOf(condition.get(0));
        final Object conditionValue = condition.get(1);
        switch (conditionOperator.toLowerCase()){
            case "in":
                final List<?> inList = (List) conditionValue;
                return new If.In(inList.stream().map(String::valueOf).collect(Collectors.toList()));
            case "isblank":
                return new If.Blank(readBoolean(conditionValue));
            case "eq":
                return new If.Equal(String.valueOf(conditionValue), true);
            case "ne":
                return new If.Equal(String.valueOf(conditionValue), false);
            case "gt":
                return new If.GreaterThen(String.valueOf(conditionValue));
            default:
                throw new RuntimeException(String.format("Unknown Questionnaire Validation Operand: %s", conditionOperator));
        }
    }

    private long readLong(Document d, String key) {
        final Object o = d.get(key);
        try{
            return Long.parseLong(String.valueOf(o));
        } catch (NumberFormatException n){
            throw new UnrecoverableException(n);
        }
    }

    private BigDecimal readBigDecimal(Document d, String key) {
        final Object o = d.get(key);
        try{
            return new BigDecimal(String.valueOf(o));
        } catch (NumberFormatException n){
            throw new UnrecoverableException(n);
        }
    }

    private boolean readBoolean(Object o) {
        return Boolean.parseBoolean(String.valueOf(o));
    }

    private void createIf(Object o, List<QuestionnaireQuestionValidation> validations) {
        final List ifCommand = (List) o;
        final Object firstField = ifCommand.get(0);

        if (firstField instanceof List) {
            for(Object ifComm : ifCommand) createIf(ifComm, validations);
        } else {
            final String fieldId = String.valueOf(ifCommand.get(0));
            final If.Condition condition = decodeCondition((List) ifCommand.get(1));
            final List<QuestionnaireQuestionValidation> thenValidations = decodeValidation((Document) ifCommand.get(2));
            final List<QuestionnaireQuestionValidation> elseValidations = ifCommand.size() == 4 ? decodeValidation((Document) ifCommand.get(3)) : new ArrayList<>();
            validations.add(new If(fieldId, condition, thenValidations, elseValidations));
        }
    }

    private QuestionnaireQuestionValidation createDateValidation(Document d, String key, BiFunction<String, Boolean, QuestionnaireQuestionValidation> creator) {
        final Object o = d.get(key);
        String field;
        boolean inclusive;

        if(o instanceof List){
            final List list = (List) o;
            field = String.valueOf(list.get(0));
            inclusive = list.size() > 1 && readBoolean(list.get(1));
        } else {
            field = String.valueOf(o);
            inclusive = false;
        }

        return creator.apply(field, inclusive);
    }
}