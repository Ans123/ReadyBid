package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp_hotel.bid.offer.Value;
import net.readybid.app.entities.rfp_hotel.bid.offer.ValueType;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class ValueCodecWithProvider extends RbMongoCodecWithProvider<Value> {

    private static final String VALUE_TYPE = "type";
    private static final String VALUE_AMOUNT = "amount";
    private static final String VALUE_AUXILIARY_AMOUNT = "auxAmount";
    private static final String VALUE_IS_DERIVED = "isDerived";
    private static final String VALUE_IS_INCLUDED = "isIncluded";

    public ValueCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(Value.class, bsonTypeClassMap);
    }

    @Override
    protected Value newInstance() {
        return new Value();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<Value>> decodeMap) {
        decodeMap.put(VALUE_TYPE, (c, reader, context) -> c.type = read(reader, context, ValueType.class));
        decodeMap.put(VALUE_AMOUNT, (c, reader, context) -> c.amount = (read(reader, context, BigDecimal.class)));
        decodeMap.put(VALUE_AUXILIARY_AMOUNT, (c, reader, context) -> c.auxiliaryAmount = (read(reader, context, BigDecimal.class)));
        decodeMap.put(VALUE_IS_DERIVED, (c, reader, context) -> c.isDerived = reader.readBoolean());
        decodeMap.put(VALUE_IS_INCLUDED, (c, reader, context) -> c.isIncluded = reader.readBoolean());
    }

    @Override
    public void encodeDocument(Document d, Value value, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, VALUE_TYPE, value.type);
        putIfNotNull(d, VALUE_AMOUNT, value.amount);
        putIfNotNull(d, VALUE_AUXILIARY_AMOUNT, value.auxiliaryAmount);
        putIfNotNull(d, VALUE_IS_DERIVED, value.isDerived);
        putIfNotNull(d, VALUE_IS_INCLUDED, value.isIncluded);
    }
}
