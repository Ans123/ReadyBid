package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.rfphotel.bid.core.HotelRfpBidReceivedState;
import net.readybid.rfphotel.bid.core.HotelRfpBidSimpleState;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.BsonReader;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;

public class HotelRfpBidStateCodec extends RbMongoCodecWithProvider<HotelRfpBidState> {

    public HotelRfpBidStateCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidState.class, bsonTypeClassMap);
    }

    @Override
    public HotelRfpBidState decode(BsonReader reader, DecoderContext context) {
        final HotelRfpBidStateStatus type = readField(reader, context, "status", HotelRfpBidStateStatus.class);
        if(type == null) return null;
        switch (type){
            case RECEIVED:
                return registry.get(HotelRfpBidReceivedState.class).decode(reader, context);
            default:
                return registry.get(HotelRfpBidSimpleState.class).decode(reader, context);
        }
    }
}
