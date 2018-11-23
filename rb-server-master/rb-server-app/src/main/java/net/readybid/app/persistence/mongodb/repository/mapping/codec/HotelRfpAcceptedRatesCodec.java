package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.entities.Id;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;


public class HotelRfpAcceptedRatesCodec extends RbMongoCodecForBuilder<HotelRfpAcceptedRates, HotelRfpAcceptedRates.Builder> {

    private final static String FIELD_RATES = "rates";
    private final static String FIELD_BY = "by";
    private final static String FIELD_AT = "at";

    public HotelRfpAcceptedRatesCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpAcceptedRates.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<HotelRfpAcceptedRates.Builder>> decoderMap) {
        decoderMap.put(FIELD_RATES, (builder, reader, context) -> builder.setAcceptedRates(readList(reader, String.class)));
        decoderMap.put(FIELD_BY, (builder, reader, context) -> builder.setBy(read(reader, context, Id.class)));
        decoderMap.put(FIELD_AT, (builder, reader, context) -> builder.setAt(readDate(reader)));
    }

    @Override
    protected HotelRfpAcceptedRates.Builder getBuilderInstance() {
        return new HotelRfpAcceptedRates.Builder();
    }

    @Override
    public void encodeDocument(Document d, HotelRfpAcceptedRates acceptedRates, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, FIELD_RATES, acceptedRates.acceptedRates);
        putIfNotNull(d, FIELD_BY, acceptedRates.by);
        putIfNotNull(d, FIELD_AT, acceptedRates.at);
    }
}
