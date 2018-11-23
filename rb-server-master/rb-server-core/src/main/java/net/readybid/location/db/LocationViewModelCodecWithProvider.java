package net.readybid.location.db;

import net.readybid.location.AddressView;
import net.readybid.location.CoordinatesView;
import net.readybid.location.LocationViewModel;
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
public class LocationViewModelCodecWithProvider extends RbMongoCodecWithProvider<LocationViewModel> {

    public LocationViewModelCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(LocationViewModel.class, bsonTypeClassMap);
    }

    @Override
    protected LocationViewModel newInstance() {
        return new LocationViewModel();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<LocationViewModel>> decodeMap) {
        decodeMap.put("address", (c, reader, context) -> c.address = read(reader, context, AddressView.class));
        decodeMap.put("fullAddress", (c, reader, context) -> c.fullAddress = reader.readString());
        decodeMap.put("coordinates", (c, reader, context) -> c.coordinates = read(reader, context, CoordinatesView.class));
    }

    @Override
    public void encodeDocument(Document d, LocationViewModel location, BsonWriter bsonWriter, EncoderContext encoderContext) {}
}
