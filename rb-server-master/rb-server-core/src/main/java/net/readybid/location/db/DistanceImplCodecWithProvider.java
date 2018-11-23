package net.readybid.location.db;

import net.readybid.app.core.entities.location.distance.DistanceImpl;
import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class DistanceImplCodecWithProvider extends RbMongoCodecWithProvider<DistanceImpl> {

    public DistanceImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(DistanceImpl.class, bsonTypeClassMap);
    }

    @Override
    protected DistanceImpl newInstance() {
        return new DistanceImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<DistanceImpl>> decodeMap) {
        decodeMap.put("value", (a, reader, context) -> a.setDistance(readDouble(reader)));
        decodeMap.put("unit", (a, reader, context) -> a.setDistanceUnit(read(reader, context, DistanceUnit.class)));
    }

    @Override
    public void encodeDocument(Document d, DistanceImpl distance, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "value", distance.getDistance());
        putIfNotNull(d, "unit", distance.getDistanceUnit());
    }
}
