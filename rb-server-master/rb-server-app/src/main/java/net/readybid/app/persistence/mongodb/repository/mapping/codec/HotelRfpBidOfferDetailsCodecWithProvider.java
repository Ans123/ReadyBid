package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp_hotel.bid.offer.HotelRfpBidOfferDetails;
import net.readybid.app.entities.rfp_hotel.bid.offer.Value;
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
public class HotelRfpBidOfferDetailsCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpBidOfferDetails> {

    public HotelRfpBidOfferDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidOfferDetails.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidOfferDetails newInstance() {
        return new HotelRfpBidOfferDetails();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidOfferDetails>> decodeMap) {

        decodeMap.put("lra", (c, reader, context) -> c.lraRate = read(reader, context, Value.class));
        decodeMap.put("nlra", (c, reader, context) -> c.nlraRate = read(reader, context, Value.class));
        decodeMap.put("dyn", (c, reader, context) -> c.dynamicRate = read(reader, context, Value.class));
        
        decodeMap.put("ec", (c, reader, context) -> c.earlyCheckout = read(reader, context, Value.class));
        decodeMap.put("prk", (c, reader, context) -> c.parking = read(reader, context, Value.class));
        decodeMap.put("bf", (c, reader, context) -> c.breakfast = read(reader, context, Value.class));
        decodeMap.put("ft", (c, reader, context) -> c.fitness = read(reader, context, Value.class));
        decodeMap.put("ia", (c, reader, context) -> c.internetAccess  = read(reader, context, Value.class));
        decodeMap.put("wf", (c, reader, context) -> c.wiFi = read(reader, context, Value.class));
        decodeMap.put("as", (c, reader, context) -> c.airportShuttle = read(reader, context, Value.class));
        decodeMap.put("amenitiesTotal", (c, reader, context) -> c.amenitiesTotal = read(reader, context, Value.class));

        decodeMap.put("lodging", (c, reader, context) -> c.lodgingTax = read(reader, context, Value.class));
        decodeMap.put("state", (c, reader, context) -> c.stateTax = read(reader, context, Value.class));
        decodeMap.put("city", (c, reader, context) -> c.cityTax = read(reader, context, Value.class));
        decodeMap.put("vatGstRm", (c, reader, context) -> c.vatGstRm = read(reader, context, Value.class));
        decodeMap.put("vatGstFb", (c, reader, context) -> c.vatGstFb = read(reader, context, Value.class));
        decodeMap.put("service", (c, reader, context) -> c.serviceTax = read(reader, context, Value.class));
        decodeMap.put("occupancy", (c, reader, context) -> c.occupancyTax = read(reader, context, Value.class));
        decodeMap.put("other", (c, reader, context) -> c.otherTax = read(reader, context, Value.class));
        decodeMap.put("taxesTotal", (c, reader, context) -> c.taxesTotal = read(reader, context, Value.class));

        decodeMap.put("tcos", (c, reader, context) -> c.totalCost = read(reader, context, Value.class));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpBidOfferDetails offer, BsonWriter bsonWriter, EncoderContext encoderContext) {

        putIfNotNull(d, "lra", offer.lraRate);
        putIfNotNull(d, "nlra", offer.nlraRate);
        putIfNotNull(d, "dyn", offer.dynamicRate);

        putIfNotNull(d, "ec", offer.earlyCheckout);
        putIfNotNull(d, "prk", offer.parking);
        putIfNotNull(d, "bf", offer.breakfast);
        putIfNotNull(d, "ft", offer.fitness);
        putIfNotNull(d, "ia", offer.internetAccess);
        putIfNotNull(d, "wf", offer.wiFi);
        putIfNotNull(d, "as", offer.airportShuttle);
        putIfNotNull(d, "amenitiesTotal", offer.amenitiesTotal);

        putIfNotNull(d, "lodging", offer.lodgingTax);
        putIfNotNull(d, "state", offer.stateTax);
        putIfNotNull(d, "city", offer.cityTax);
        putIfNotNull(d, "vatGstRm", offer.vatGstRm);
        putIfNotNull(d, "vatGstFb", offer.vatGstFb);
        putIfNotNull(d, "service", offer.serviceTax);
        putIfNotNull(d, "occupancy", offer.occupancyTax);
        putIfNotNull(d, "other", offer.otherTax);
        putIfNotNull(d, "taxesTotal", offer.taxesTotal);

        putIfNotNull(d, "tcos", offer.totalCost);
    }
}
