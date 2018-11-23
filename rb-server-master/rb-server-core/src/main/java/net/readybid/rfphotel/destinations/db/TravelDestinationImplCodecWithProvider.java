package net.readybid.rfphotel.destinations.db;

import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatusDetails;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class TravelDestinationImplCodecWithProvider extends RbMongoCodecWithProvider<TravelDestinationImpl> {

    public TravelDestinationImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(TravelDestinationImpl.class, bsonTypeClassMap);
    }

    @Override
    protected TravelDestinationImpl newInstance() {
        return new TravelDestinationImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<TravelDestinationImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId().toString()));
        decodeMap.put("rfpId", (c, reader, context) -> c.setRfpId(reader.readObjectId().toString()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, TravelDestinationType.class)));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("estSpend", (c, reader, context) -> c.setEstimatedSpend(readLongObject(reader)));
        decodeMap.put("estRoomNights", (c, reader, context) -> c.setEstimatedRoomNights(readIntObject(reader)));
        decodeMap.put("location", (c, reader, context) -> c.setLocation(read(reader, context, LocationImpl.class)));
        decodeMap.put("filter", (c, reader, context) -> c.setFilter(read(reader, context, TravelDestinationHotelFilter.class)));
        decodeMap.put("created", (c, reader, context) -> c.setCreationDetails(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatusDetails(read(reader, context, TravelDestinationStatusDetails.class)));
    }

    @Override
    public void encodeDocument(Document document, TravelDestinationImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "_id", new ObjectId(tObject.getId()));
        putIfNotNull(document, "rfpId", new ObjectId(tObject.getRfpId()));
        putIfNotNull(document, "type", tObject.getType());
        putIfNotNull(document, "name", tObject.getName());
        putIfNotNull(document, "estSpend", tObject.getEstimatedSpend());
        putIfNotNull(document, "estRoomNights", tObject.getEstimatedRoomNights());
        putIfNotNull(document, "location", tObject.getLocation());
        putIfNotNull(document, "filter", tObject.getFilter());
        putIfNotNull(document, "created", tObject.getCreated());
        putIfNotNull(document, "status", tObject.getStatus());
    }
}