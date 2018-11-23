package net.readybid.rfphotel.bid.core.negotiations.values;

import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationPercentageValueCodecWithProvider extends NegotiationValueCodecWithProvider<NegotiationPercentageValue> {

    public NegotiationPercentageValueCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiationPercentageValue.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiationPercentageValue newInstance(){ return new NegotiationPercentageValue();}

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<NegotiationPercentageValue>> decodeMap){
        super.decodeMapSetup(decodeMap);
        decodeMap.put("amount", (c, reader, context) -> c.amount = read(reader, context, BigDecimal.class));
    }

    @Override
    public void encodeDocument(Document d, NegotiationPercentageValue tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        super.encodeDocument(d, tObject, bsonWriter, encoderContext);
        putIfNotNull(d, "amount", tObject.amount);
    }
}