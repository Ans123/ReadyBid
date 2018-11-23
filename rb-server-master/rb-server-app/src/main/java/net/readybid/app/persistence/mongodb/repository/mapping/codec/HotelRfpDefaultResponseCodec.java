package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.entities.Id;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;


public class HotelRfpDefaultResponseCodec extends RbMongoCodecForBuilder<HotelRfpDefaultResponse, HotelRfpDefaultResponse.Builder> {

    private static final String FIELD_ID = "_id";
    private static final String FIELD_ANSWERS = QuestionnaireResponseImplCodecWithProvider.FIELD_ANSWERS;
    private static final String FIELD_IS_VALID = QuestionnaireResponseImplCodecWithProvider.FIELD_IS_VALID;
    private static final String FIELD_STATE = QuestionnaireResponseImplCodecWithProvider.FIELD_STATE;

    public HotelRfpDefaultResponseCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpDefaultResponse.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<HotelRfpDefaultResponse.Builder>> decoderMap) {
        decoderMap.put(FIELD_ID, (b, reader, context) -> b.setHotelId(read(reader, context, Id.class)));
        decoderMap.put(FIELD_ANSWERS, (b, reader, context) -> b.setAnswers(readMapStringString(reader)));
        decoderMap.put(FIELD_IS_VALID, (b, reader, context) -> b.setIsValid(reader.readBoolean()));
        decoderMap.put(FIELD_STATE, (b, reader, context) -> b.setState(readListOfObjects(reader, context, QuestionnaireConfigurationItem.class)));
    }

    @Override
    protected HotelRfpDefaultResponse.Builder getBuilderInstance() {
        return new HotelRfpDefaultResponse.Builder();
    }

    @Override
    public void encodeDocument(Document d, HotelRfpDefaultResponse tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, FIELD_ID, tObject.hotelId);
        putIfNotNull(d, FIELD_ANSWERS, tObject.answers);
        d.put(FIELD_IS_VALID, tObject.isValid);
        putIfNotNull(d, FIELD_STATE, tObject.state);
    }
}
