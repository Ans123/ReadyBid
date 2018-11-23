package net.readybid.rfphotel.destinations.db;

import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class TravelDestinationListItemViewModelCodecWithProvider extends RbMongoCodecWithProvider<TravelDestinationListItemViewModel> {

    public TravelDestinationListItemViewModelCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(TravelDestinationListItemViewModel.class, bsonTypeClassMap);
    }

    @Override
    protected TravelDestinationListItemViewModel newInstance() {
        return new TravelDestinationListItemViewModel();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<TravelDestinationListItemViewModel>> decodeMap) {
        decodeMap.put("_id", (t, bsonReader, decoderContext) -> t.id = bsonReader.readObjectId().toString());
        decodeMap.put("rfpId", (t, bsonReader, decoderContext) -> t.rfpId = bsonReader.readObjectId().toString());
        decodeMap.put("rfpName", (t, bsonReader, decoderContext) -> t.rfpName = bsonReader.readString());
        decodeMap.put("type", (t, bsonReader, decoderContext) -> t.type = read(bsonReader, decoderContext, TravelDestinationType.class));
        decodeMap.put("name", (t, bsonReader, decoderContext) -> t.name = bsonReader.readString());
        decodeMap.put("estRoomNights", (t, bsonReader, decoderContext) -> t.estimatedRoomNights = readIntObject(bsonReader));
        decodeMap.put("estSpend", (t, bsonReader, decoderContext) -> t.estimatedSpend = readLongObject(bsonReader));
        decodeMap.put("location", (t, bsonReader, decoderContext) -> {
            final LocationImpl l = read(bsonReader, decoderContext, LocationImpl.class);
            if (l != null) {
                t.fullAddress = l.getFullAddress();
            }
        });
    }

    @Override
    public void encodeDocument(Document document, TravelDestinationListItemViewModel tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {}
}