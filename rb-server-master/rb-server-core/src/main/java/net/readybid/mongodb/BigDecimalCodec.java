package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.math.BigDecimal;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class BigDecimalCodec implements Codec<BigDecimal> {

    @Override
    public BigDecimal decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final BsonType currentBsonType = bsonReader.getCurrentBsonType();
        switch (currentBsonType){
            case NULL:
                bsonReader.readNull();
                return null;
            case UNDEFINED:
                bsonReader.readUndefined();
                return null;
            case DOUBLE:
                return BigDecimal.valueOf(bsonReader.readDouble());
            case DECIMAL128:
                return bsonReader.readDecimal128().bigDecimalValue();
            case INT32:
                return BigDecimal.valueOf(bsonReader.readInt32());
            case INT64:
                return BigDecimal.valueOf(bsonReader.readInt64());
            case STRING:
                return BigDecimal.valueOf(Double.parseDouble(bsonReader.readString()));
            default:
                throw new RuntimeException("Invalid Big Decimal Type: " + String.valueOf(currentBsonType));
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, BigDecimal bigDecimal, EncoderContext encoderContext) {
        if (bigDecimal == null) {
            bsonWriter.writeNull();
        } else {
            bsonWriter.writeDouble(bigDecimal.doubleValue());
//            bsonWriter.writeDecimal128(new Decimal128(bigDecimal));
        }
    }

    @Override
    public Class<BigDecimal> getEncoderClass() {
        return BigDecimal.class;
    }
}
