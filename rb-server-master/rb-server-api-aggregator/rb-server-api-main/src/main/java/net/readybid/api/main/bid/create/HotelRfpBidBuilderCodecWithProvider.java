package net.readybid.api.main.bid.create;

import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.auth.account.core.AccountImpl;
import net.readybid.entities.hotel.core.HotelImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class HotelRfpBidBuilderCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpBidBuilder> {

    public HotelRfpBidBuilderCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidBuilder.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidBuilder newInstance() {
        return new HotelRfpBidBuilder();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidBuilder>> decodeMap) {
        decodeMap.put("account", (c, reader, context) -> c.setAccount(read(reader, context, AccountImpl.class)));
        decodeMap.put("hotel", (c, reader, context) -> c.setHotel(read(reader, context, HotelImpl.class)));
        decodeMap.put("defaultResponse", (c, reader, context) -> c.setDefaultResponse(read(reader, context, HotelRfpDefaultResponse.class)));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpBidBuilder account, BsonWriter bsonWriter, EncoderContext encoderContext) {}
}
