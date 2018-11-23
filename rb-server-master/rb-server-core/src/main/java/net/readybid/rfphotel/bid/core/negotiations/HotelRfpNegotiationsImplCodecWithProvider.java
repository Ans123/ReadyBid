package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 7/25/2017.
 */
public class HotelRfpNegotiationsImplCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpNegotiationsImpl> {

    public HotelRfpNegotiationsImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpNegotiationsImpl.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpNegotiationsImpl newInstance() {
        return new HotelRfpNegotiationsImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpNegotiationsImpl>> decodeMap) {

        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("config", (c, reader, context) -> c.setConfig(read(reader, context, HotelRfpNegotiationsConfig.class)));
        decodeMap.put("parties", (c, reader, context) -> c.setParties(read(reader, context, NegotiationsPartiesImpl.class)));
        decodeMap.put("communication", (c, reader, context) -> c.setCommunication(readListOfObjects(reader, context, HotelRfpNegotiation.class)));
    }

    @Override
    public void encodeDocument(Document document, HotelRfpNegotiationsImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "_id", tObject.getBidId());
        putIfNotNull(document, "config", tObject.getConfig());
        putIfNotNull(document, "parties", tObject.getParties());
        putIfNotNull(document, "communication", tObject.getCommunication());
    }
}