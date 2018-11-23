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
public class NegotiationMockedValueCodecWithProvider extends NegotiationValueCodecWithProvider<NegotiationMockedValue> {

    public NegotiationMockedValueCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiationMockedValue.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiationMockedValue newInstance(){ return new NegotiationMockedValue();}

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<NegotiationMockedValue>> decodeMap){
        super.decodeMapSetup(decodeMap);
        decodeMap.put("mocked", (c, reader, context) -> c.mocked = reader.readBoolean());
        decodeMap.put("mockPercentage", (c, reader, context) -> c.mockPercentage = read(reader, context, BigDecimal.class));
    }

    @Override
    public void encodeDocument(Document d, NegotiationMockedValue tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        super.encodeDocument(d, tObject, bsonWriter, encoderContext);
        putIfNotNull(d, "mocked", tObject.mocked);
        putIfNotNull(d, "mockPercentage", tObject.mockPercentage);
    }
}