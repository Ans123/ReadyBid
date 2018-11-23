package net.readybid.rfphotel.destinations.db;

import net.readybid.app.core.entities.location.distance.DistanceImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class TravelDestinationHotelFilterCodecWithProvider extends RbMongoCodecWithProvider<TravelDestinationHotelFilter> {

    public TravelDestinationHotelFilterCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(TravelDestinationHotelFilter.class, bsonTypeClassMap);
    }

    @Override
    protected TravelDestinationHotelFilter newInstance() {
        return new TravelDestinationHotelFilter();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<TravelDestinationHotelFilter>> decodeMap) {
        decodeMap.put("maxDistance", (c, reader, context) -> c.maxDistance = (read(reader, context, DistanceImpl.class)));
        decodeMap.put("amenities", (c, reader, context) -> c.amenities = readList(reader, String.class));
        decodeMap.put("chains", (c, reader, context) -> c.chains = readList(reader, String.class));
    }

    @Override
    public void encodeDocument(Document document, TravelDestinationHotelFilter filter, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "maxDistance", filter.maxDistance);
        putIfNotNull(document, "amenities", filter.amenities);
        putIfNotNull(document, "chains", filter.chains);
    }
}