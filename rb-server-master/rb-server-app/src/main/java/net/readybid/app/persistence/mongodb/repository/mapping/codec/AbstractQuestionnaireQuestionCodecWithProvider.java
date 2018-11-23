package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestion;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

public abstract class AbstractQuestionnaireQuestionCodecWithProvider<T extends QuestionnaireQuestion> extends RbMongoCodecWithProvider<T> {

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

    AbstractQuestionnaireQuestionCodecWithProvider(Class<T> tClass, BsonTypeClassMap bsonTypeClassMap) {
        super(tClass, bsonTypeClassMap);
    }

    @Override
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
        putIfNotNull(d, OPTIONS, tObject instanceof ListQuestionnaireQuestion ? ((ListQuestionnaireQuestion) tObject).getOptions() : null);
        putIfNotNull(d, LOCKED, tObject.isLocked());
        putIfNotNull(d, VALIDATIONS, tObject.mapValidations());
    }
}
