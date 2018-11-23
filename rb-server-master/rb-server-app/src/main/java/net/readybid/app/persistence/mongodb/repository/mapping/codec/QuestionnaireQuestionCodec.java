package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestion;
import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestionOption;
import net.readybid.app.entities.rfp.questionnaire.form.question.QuestionnaireQuestionBuilder;
import net.readybid.app.entities.rfp.questionnaire.form.validation.QuestionnaireQuestionValidator;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class QuestionnaireQuestionCodec extends RbMongoCodecForBuilder<QuestionnaireQuestion, QuestionnaireQuestionBuilder>{

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String ORD = "ord";
    public static final String NAME = "name";
    public static final String PLACEHOLDER = "placeholder";
    public static final String DESCRIPTION = "description";
    public static final String CLASSES = "classes";
    public static final String REQUIRED = "req";
    public static final String READ_ONLY = "readOnly";
    public static final String LOCKED = "locked";
    public static final String OPTIONS = "options";
    public static final String VALIDATIONS = "validations";

    public QuestionnaireQuestionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireQuestion.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<QuestionnaireQuestionBuilder>> decoderMap) {
        decoderMap.put(TYPE, (builder, reader, context) -> builder.setType(read(reader, context, QuestionnaireQuestionType.class)));
        decoderMap.put(ID, (builder, reader, context) -> builder.setId(reader.readString()));
        decoderMap.put(ORD, (builder, reader, context) -> builder.setOrd(readInt(reader)));
        decoderMap.put(NAME, (builder, reader, context) -> builder.setName(reader.readString()));
        decoderMap.put(PLACEHOLDER, (builder, reader, context) -> builder.setPlaceholder(reader.readString()));
        decoderMap.put(DESCRIPTION, (builder, reader, context) -> builder.setDescription(reader.readString()));
        decoderMap.put(CLASSES, (builder, reader, context) -> builder.setClasses(reader.readString()));
        decoderMap.put(REQUIRED, (builder, reader, context) -> builder.setRequired(reader.readBoolean()));
        decoderMap.put(READ_ONLY, (builder, reader, context) -> builder.setReadOnly(reader.readBoolean()));
        decoderMap.put(LOCKED, (builder, reader, context) -> builder.setLocked(reader.readBoolean()));
        decoderMap.put(OPTIONS, (builder, reader, context) -> builder.setOptions(readListOfObjects(reader, context, ListQuestionnaireQuestionOption.class)));
        decoderMap.put(VALIDATIONS, (builder, reader, context) -> builder.setValidator(read(reader, context, QuestionnaireQuestionValidator.class)));
    }

    @Override
    protected QuestionnaireQuestionBuilder getBuilderInstance() {
        return new QuestionnaireQuestionBuilder();
    }

    @Override //todo: encoder wants explicit type, not this
    public void encodeDocument(Document d, QuestionnaireQuestion tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, TYPE, tObject.getType());
        putIfNotNull(d, ID, tObject.getId());
        putIfNotNull(d, ORD, tObject.getOrd());
        putIfNotNull(d, NAME, tObject.getName());
        putIfNotNull(d, PLACEHOLDER, tObject.getPlaceholder());
        putIfNotNull(d, DESCRIPTION, tObject.getDescription());
        putIfNotNull(d, CLASSES, tObject.getClasses());
        putIfNotNull(d, REQUIRED, tObject.isRequired());
        putIfNotNull(d, READ_ONLY, tObject.isReadOnly());
        putIfNotNull(d, LOCKED, tObject.isLocked());
        putIfNotNull(d, OPTIONS, tObject instanceof ListQuestionnaireQuestion ? ((ListQuestionnaireQuestion) tObject).getOptions() : null);
        putIfNotNull(d, VALIDATIONS, tObject.mapValidations());
    }
}
