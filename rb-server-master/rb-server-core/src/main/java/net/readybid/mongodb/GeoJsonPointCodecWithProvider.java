package net.readybid.mongodb;

import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class GeoJsonPointCodecWithProvider extends RbMongoCodecWithProvider<GeoJsonPoint> {

    public GeoJsonPointCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(GeoJsonPoint.class, bsonTypeClassMap);
    }

    @Override
    protected GeoJsonPoint newInstance() {
        return new GeoJsonPoint(0,0);
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<GeoJsonPoint>> decodeMap) {}

    @Override
    public void encodeDocument(Document d, GeoJsonPoint point, BsonWriter bsonWriter, EncoderContext encoderContext) {
        d.put("type", point.type);
        d.put("coordinates", point.coordinates);
    }
}
