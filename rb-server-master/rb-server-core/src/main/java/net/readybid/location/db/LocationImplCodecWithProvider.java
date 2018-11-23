package net.readybid.location.db;

import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;
import net.readybid.app.core.entities.location.LocationImpl;
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
public class LocationImplCodecWithProvider extends RbMongoCodecWithProvider<LocationImpl> {

    public LocationImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(LocationImpl.class, bsonTypeClassMap);
    }

    @Override
    protected LocationImpl newInstance() {
        return new LocationImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<LocationImpl>> decodeMap) {
        decodeMap.put("address", (c, reader, context) -> c.setAddress(read(reader, context, AddressImpl.class)));
        decodeMap.put("fullAddress", (c, reader, context) -> c.setFullAddress(reader.readString()));
        decodeMap.put("coordinates", (c, reader, context) -> c.setCoordinates(read(reader, context, CoordinatesImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, LocationImpl location, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "address", location.getAddress());
        putIfNotNull(d, "fullAddress", location.getFullAddress());
        putIfNotNull(d, "coordinates", location.getCoordinates());
    }
}
