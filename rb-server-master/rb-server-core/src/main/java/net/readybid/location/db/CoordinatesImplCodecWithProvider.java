package net.readybid.location.db;

import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;
import net.readybid.mongodb.GeoJsonPoint;
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
public class CoordinatesImplCodecWithProvider extends RbMongoCodecWithProvider<CoordinatesImpl> {

    public CoordinatesImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(CoordinatesImpl.class, bsonTypeClassMap);
    }

    @Override
    protected CoordinatesImpl newInstance() {
        return new CoordinatesImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<CoordinatesImpl>> decodeMap) {
        decodeMap.put("lat", (c, reader, context) -> c.setLatitude(readDouble(reader)));
        decodeMap.put("lng", (c, reader, context) -> c.setLongitude(readDouble(reader)));
    }

    @Override
    public void encodeDocument(Document d, CoordinatesImpl coordinates, BsonWriter bsonWriter, EncoderContext encoderContext) {
            d.put("lat", coordinates.getLatitude());
            d.put("lng", coordinates.getLongitude());
            d.put("point", new GeoJsonPoint(coordinates.getLongitude(), coordinates.getLatitude()));
    }
}
