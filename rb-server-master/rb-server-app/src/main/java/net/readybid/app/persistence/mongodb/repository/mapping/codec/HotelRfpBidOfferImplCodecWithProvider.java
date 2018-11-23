package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp_hotel.bid.offer.HotelRfpBidOfferDetails;
import net.readybid.app.entities.rfp_hotel.bid.offer.HotelRfpBidOfferImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class HotelRfpBidOfferImplCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpBidOfferImpl> {

    public HotelRfpBidOfferImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidOfferImpl.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidOfferImpl newInstance() {
        return new HotelRfpBidOfferImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidOfferImpl>> decodeMap) {
        decodeMap.put("main", (c, reader, context) -> c.main = read(reader, context, HotelRfpBidOfferDetails.class));
        decodeMap.put("averages", (c, reader, context) -> c.averages = read(reader, context, HotelRfpBidOfferDetails.class));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpBidOfferImpl offer, BsonWriter bsonWriter, EncoderContext encoderContext) {
        d.put("main", offer.main);
        d.put("averages", offer.averages);
    }
}
