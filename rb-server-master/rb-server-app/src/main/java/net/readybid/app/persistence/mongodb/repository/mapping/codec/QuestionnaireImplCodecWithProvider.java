package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponseImpl;
import net.readybid.app.entities.rfp.questionnaire.QuestionnaireImpl;
import net.readybid.app.entities.rfp.questionnaire.form.QuestionnaireFormImpl;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.type.RfpType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class QuestionnaireImplCodecWithProvider extends RbMongoCodecWithProvider<QuestionnaireImpl> {

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_MODEL = "model";
    private static final String FIELD_CONFIG = "config";
    private static final String FIELD_ACCEPTED = "accepted";
    private static final String FIELD_GLOBALS = "globals";
    private static final String FIELD_RESPONSE = "response";
    private static final String FIELD_RESPONSE_DRAFT = "responseDraft";

    public QuestionnaireImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(QuestionnaireImpl.class, bsonTypeClassMap);
    }

    @Override
    protected QuestionnaireImpl newInstance() {
        return new QuestionnaireImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<QuestionnaireImpl>> decodeMap) {
        decodeMap.put(FIELD_TYPE, (c, reader, context) -> c.setType(read(reader, context, RfpType.class)));
        decodeMap.put(FIELD_MODEL, (c, reader, context) -> c.setModel(read(reader, context, QuestionnaireFormImpl.class)));
        decodeMap.put(FIELD_CONFIG, (c, reader, context) -> c.setConfig(readListOfObjects(reader, context, QuestionnaireConfigurationItem.class)));
        decodeMap.put(FIELD_ACCEPTED, (c, reader, context) -> c.setAccepted(read(reader, context, HotelRfpAcceptedRates.class)));
        // FIELD_GLOBALS skipped
        decodeMap.put(FIELD_RESPONSE, (c, reader, context) -> c.setResponse(read(reader, context, QuestionnaireResponseImpl.class)));
        decodeMap.put(FIELD_RESPONSE_DRAFT, (c, reader, context) -> c.setResponseDraft(read(reader, context, QuestionnaireResponseImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, QuestionnaireImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, FIELD_TYPE, tObject.getType());
        putIfNotNull(d, FIELD_MODEL, tObject.getModel());
        putIfNotNull(d, FIELD_CONFIG, tObject.getConfig());
        putIfNotNull(d, FIELD_ACCEPTED, tObject.getAccepted());
        // FIELD_GLOBALS skipped
        putIfNotNull(d, FIELD_RESPONSE, tObject.getResponse());
        putIfNotNull(d, FIELD_RESPONSE_DRAFT, tObject.getResponseDraft());
    }
}
