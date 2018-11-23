package net.readybid.rfphotel.bid.core.negotiations.values;

import net.readybid.mongodb.RbMongoCodecWithProvider;
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
public abstract class NegotiationValueCodecWithProvider<T extends NegotiationValue> extends RbMongoCodecWithProvider<T> {

    public NegotiationValueCodecWithProvider(Class<T> tClass, BsonTypeClassMap bsonTypeClassMap) {
        super(tClass, bsonTypeClassMap);
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<T>> decodeMap) {
        decodeMap.put("type", (c, reader, context) -> c.type = read(reader, context, NegotiationValueType.class));
        decodeMap.put("valid", (c, reader, context) -> c.valid = reader.readBoolean());
        decodeMap.put("value", (c, reader, context) -> c.value = read(reader, context, BigDecimal.class));
        decodeMap.put("included", (c, reader, context) -> c.included = reader.readBoolean());
        decodeMap.put("change", (c, reader, context) -> c.change = read(reader, context, NegotiationValueChangeType.class));
    }

    @Override
    public void encodeDocument(Document document, T tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "type", tObject.type);
        putIfNotNull(document, "valid", tObject.valid);
        putIfNotNull(document, "value", tObject.value);
        putIfNotNull(document, "included", tObject.included);
        putIfNotNull(document, "change", tObject.change);
    }
}