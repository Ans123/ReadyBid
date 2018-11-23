package net.readybid.app.gate.repository;

import net.readybid.api.hotelrfp.traveldestination.TravelDestinationManagerHotelViewModel;
import net.readybid.entities.chain.HotelChainListItemViewModel;
import net.readybid.entities.core.EntityImageView;
import net.readybid.location.LocationViewModel;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class TravelDestinationManagerHotelViewModelCodecWithProvider extends RbMongoCodecWithProvider<TravelDestinationManagerHotelViewModel> {

    public TravelDestinationManagerHotelViewModelCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(TravelDestinationManagerHotelViewModel.class, bsonTypeClassMap);
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<TravelDestinationManagerHotelViewModel>> decodeMap){
        decodeMap.put("_id", (c, reader, context) -> c.id = String.valueOf(reader.readObjectId()));
        decodeMap.put("name", (c, reader, context) -> c.name = reader.readString());
        decodeMap.put("website", (c, reader, context) -> c.website = reader.readString());
        decodeMap.put("location", (c, reader, context) -> c.location = (read(reader, context, LocationViewModel.class)));
        decodeMap.put("image", (c, reader, context) -> c.image = read(reader, context, EntityImageView.class));
        decodeMap.put("chain", (c, reader, context) -> c.chain = read(reader, context, HotelChainListItemViewModel.class));
        decodeMap.put("rating", (c, reader, context) -> c.rating = readInt(reader));
        decodeMap.put("amenities", (c, reader, context) -> c.amenities = readList(reader, String.class));
        decodeMap.put("distanceMi", (c, reader, context) -> c.distanceMi = readDouble(reader));
        decodeMap.put("distanceKm", (c, reader, context) -> c.distanceKm = readDouble(reader));
    }

    @Override
    public void encodeDocument(Document d, TravelDestinationManagerHotelViewModel hotel, BsonWriter bsonWriter, EncoderContext encoderContext) {
    }

    @Override
    protected TravelDestinationManagerHotelViewModel newInstance() {
        return new TravelDestinationManagerHotelViewModel();
    }
}
