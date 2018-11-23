package net.readybid.location.db;

import net.readybid.location.CoordinatesView;
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
public class CoordinatesViewCodecWithProvider extends RbMongoCodecWithProvider<CoordinatesView> {

    public CoordinatesViewCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(CoordinatesView.class, bsonTypeClassMap);
    }

    @Override
    protected CoordinatesView newInstance() {
        return new CoordinatesView();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<CoordinatesView>> decodeMap) {
        decodeMap.put("lat", (c, reader, context) -> c.lat = readDouble(reader));
        decodeMap.put("lng", (c, reader, context) -> c.lng = readDouble(reader));
    }

    @Override
    public void encodeDocument(Document d, CoordinatesView coordinates, BsonWriter bsonWriter, EncoderContext encoderContext) {
    }
}
