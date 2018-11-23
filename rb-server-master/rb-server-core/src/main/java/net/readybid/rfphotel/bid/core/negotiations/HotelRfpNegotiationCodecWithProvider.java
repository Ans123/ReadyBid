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
public class HotelRfpNegotiationCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpNegotiation> {

    public HotelRfpNegotiationCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpNegotiation.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpNegotiation newInstance() {
        return new HotelRfpNegotiation();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpNegotiation>> decodeMap) {

        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("from", (c, reader, context) -> c.setFrom(read(reader, context, NegotiatorImpl.class)));
        decodeMap.put("message", (c, reader, context) -> c.setMessage(reader.readString()));
        decodeMap.put("at", (c, reader, context) -> c.setAt(readDate(reader)));
        decodeMap.put("values", (c, reader, context) -> c.setValues(read(reader, context, HotelRfpNegotiationValues.class)));
    }

    @Override
    public void encodeDocument(Document document, HotelRfpNegotiation tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "_id", tObject.getId());
        putIfNotNull(document, "from", tObject.getFrom());
        putIfNotNull(document, "message", tObject.getMessage());
        putIfNotNull(document, "at", tObject.getAt());
        putIfNotNull(document, "values", tObject.getValues());
    }
}