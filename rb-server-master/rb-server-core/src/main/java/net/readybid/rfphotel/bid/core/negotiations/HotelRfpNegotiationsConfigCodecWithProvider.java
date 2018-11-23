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
 *
 */
public class HotelRfpNegotiationsConfigCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpNegotiationsConfig> {

    public HotelRfpNegotiationsConfigCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpNegotiationsConfig.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpNegotiationsConfig newInstance() {
        return new HotelRfpNegotiationsConfig();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpNegotiationsConfig>> decodeMap) {
        decodeMap.put("seasons", (c, reader, context) -> c.seasons = readListOfMaps(reader));
        decodeMap.put("roomTypes", (c, reader, context) -> c.roomTypes = readListOfMaps(reader));
        decodeMap.put("rates", (c, reader, context) -> c.rates = readList(reader, String.class));
        decodeMap.put("occupancies", (c, reader, context) -> c.occupancies = readInt(reader));
        decodeMap.put("amenities", (c, reader, context) -> c.amenities = readList(reader, String.class));
        decodeMap.put("currency", (c, reader, context) -> c.currency = reader.readString());
    }

    @Override
    public void encodeDocument(Document document, HotelRfpNegotiationsConfig tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "seasons", tObject.seasons);
        putIfNotNull(document, "roomTypes", tObject.roomTypes);
        putIfNotNull(document, "rates", tObject.rates);
        putIfNotNull(document, "occupancies", tObject.occupancies);
        putIfNotNull(document, "amenities", tObject.amenities);
        putIfNotNull(document, "currency", tObject.currency);
    }
}