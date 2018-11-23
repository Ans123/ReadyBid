package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class HotelRfpNegotiationValuesCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpNegotiationValues> {

    public HotelRfpNegotiationValuesCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpNegotiationValues.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpNegotiationValues newInstance() {
        return new HotelRfpNegotiationValues();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpNegotiationValues>> decodeMap) {

        decodeMap.put("rates", (c, reader, context) -> c.rates = readNegotiationValuesMap(reader));
        decodeMap.put("amenities", (c, reader, context) -> c.amenities = readNegotiationValuesMap(reader));
        decodeMap.put("amenitiesTotal", (c, reader, context) -> c.amenitiesTotal = readNegotiationValue(reader));
        decodeMap.put("taxes", (c, reader, context) -> c.taxes = readNegotiationValuesMap(reader));
        decodeMap.put("taxesTotal", (c, reader, context) -> c.taxesTotal = readNegotiationValue(reader));
        decodeMap.put("totalCosts", (c, reader, context) -> c.totalCosts = readNegotiationValuesMap(reader));
    }

    @Override
    public void encodeDocument(Document document, HotelRfpNegotiationValues tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "rates", tObject.rates);
        putIfNotNull(document, "amenities", tObject.amenities);
        putIfNotNull(document, "amenitiesTotal", tObject.amenitiesTotal);
        putIfNotNull(document, "taxes", tObject.taxes);
        putIfNotNull(document, "taxesTotal", tObject.taxesTotal);
        putIfNotNull(document, "totalCosts", tObject.totalCosts);
    }
}