package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponseImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class QuestionnaireResponseImplCodecWithProvider extends RbMongoCodecWithProvider<QuestionnaireResponseImpl> {

    public static final String FIELD_ANSWERS = "answers";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_IS_VALID = "isAvailable";
    public static final String ERRORS_COUNT = "errorsCount";
    public static final String ERRORS_LIST = "errorsList";
    public static final String TOUCHED = "touched";

    public QuestionnaireResponseImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireResponseImpl.class, bsonTypeClassMap);
    }

    @Override
    protected QuestionnaireResponseImpl newInstance() {
        return new QuestionnaireResponseImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<QuestionnaireResponseImpl>> decodeMap) {
        decodeMap.put(FIELD_ANSWERS, (c, reader, context) -> c.setAnswers(readMapStringString(reader)));
        decodeMap.put(FIELD_IS_VALID, (c, reader, context) -> c.setIsValid(reader.readBoolean()));
        decodeMap.put(FIELD_STATE, (c, reader, context) -> c.setState(readListOfObjects(reader, context, QuestionnaireConfigurationItem.class)));
        decodeMap.put(ERRORS_COUNT, (c, reader, context) -> c.setErrorsCount(readLong(reader)));
        decodeMap.put(ERRORS_LIST, (c, reader, context) -> c.setErrorsList(readList(reader, String.class)));
        decodeMap.put(TOUCHED, (c, reader, context) -> c.setTouched(reader.readBoolean()));
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireResponseImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, FIELD_ANSWERS, tObject.getAnswers());
        d.put(FIELD_IS_VALID, tObject.isValid());
        putIfNotNull(d, FIELD_STATE, tObject.getState());
        d.put(ERRORS_COUNT, tObject.getErrorsCount());
        putIfNotNull(d, ERRORS_LIST, tObject.getErrorsList());
        d.put(TOUCHED, tObject.isTouched());
    }
}
