package net.readybid.app.persistence.mongodb.repository.mapping.mapping;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import org.bson.codecs.BsonTypeClassMap;

import java.util.Map;

public class HotelRfpBidQueryViewMapping {

    public static RbMongoCodecWithProvider<HotelRfpBidQueryView.Builder> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new HotelRfpBidQueryViewBuilderCodec(bsonTypeClassMap);
    }

    private static class HotelRfpBidQueryViewBuilderCodec extends RbMongoCodecWithProvider<HotelRfpBidQueryView.Builder> {

        HotelRfpBidQueryViewBuilderCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(HotelRfpBidQueryView.Builder.class, bsonTypeClassMap);
        }

        @Override
        protected HotelRfpBidQueryView.Builder newInstance() {
            return new HotelRfpBidQueryView.Builder();
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidQueryView.Builder>> decodeMap) {
            decodeMap.put("_id", (c, reader, context) -> c._id = String.valueOf(reader.readObjectId()));
            decodeMap.put("rfp", (c, reader, context) -> c.rfp = readMap(reader));
            decodeMap.put("subject", (c, reader, context) -> c.subject = readMap(reader));
            decodeMap.put("buyer", (c, reader, context) -> c.buyer = readMap(reader));
            decodeMap.put("supplier", (c, reader, context) -> c.supplier = readMap(reader));
            decodeMap.put("analytics", (c, reader, context) -> c.analytics = readMap(reader));
            decodeMap.put("offer", (c, reader, context) -> c.offer = readMap(reader));
            decodeMap.put("state", (c, reader, context) -> c.state = read(reader, context, HotelRfpBidState.class));
        }
    }
}
