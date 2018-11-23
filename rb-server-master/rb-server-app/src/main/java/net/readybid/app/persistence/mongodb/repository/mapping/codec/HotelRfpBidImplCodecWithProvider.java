package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.entities.rfp.questionnaire.QuestionnaireImpl;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.entities.rfp_hotel.bid.offer.HotelRfpBidOfferImpl;
import net.readybid.entities.rfp.RateLoadingInformationImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.buyer.BuyerImpl;
import net.readybid.rfp.core.RfpImpl;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsImpl;
import net.readybid.rfphotel.supplier.HotelRfpSupplierImpl;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.time.LocalDate;
import java.util.Map;

import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.BID_HOTEL_RFP_TYPE;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class HotelRfpBidImplCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpBidImpl> {

    public HotelRfpBidImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidImpl.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidImpl newInstance() {
        return new HotelRfpBidImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("rfp", (c, reader, context) -> c.setRfp(read(reader, context, RfpImpl.class)));
        decodeMap.put(BID_HOTEL_RFP_TYPE, (c, reader, context) -> c.setType(read(reader, context, HotelRfpType.class)));
        decodeMap.put("questionnaire", (c, reader, context) -> c.setQuestionnaire(read(reader, context, QuestionnaireImpl.class)));
        decodeMap.put("subject", (c, reader, context) -> c.setSubject(read(reader, context, TravelDestinationImpl.class)));
        decodeMap.put("buyer", (c, reader, context) -> c.setBuyer(read(reader, context, BuyerImpl.class)));
        decodeMap.put("supplier", (c, reader, context) -> c.setSupplier(read(reader, context, HotelRfpSupplierImpl.class)));
        decodeMap.put("analytics", (c, reader, context) -> c.setAnalytics(readMap(reader)));
        decodeMap.put("created", (c, reader, context) -> c.setCreationDetails(read(reader, context, CreationDetails.class)));
        decodeMap.put("state", (c, reader, context) -> c.setState(read(reader, context, HotelRfpBidState.class)));
        decodeMap.put("negotiations", (c, reader, context) -> c.setNegotiations(read(reader, context, HotelRfpNegotiationsImpl.class)));
        decodeMap.put("offer", (c, reader, context) -> c.setOffer(read(reader,context, HotelRfpBidOfferImpl.class)));
        decodeMap.put("rateLoadingInformation", (c, reader, context) -> c.setRateLoadingInformation(readListOfObjects(reader, context, RateLoadingInformationImpl.class)));
        decodeMap.put("finalAgreementDate", (c, reader, context) -> c.setFinalAgreementDate(read(reader, context, LocalDate.class)));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpBidImpl bid, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", bid.getId());
        putIfNotNull(d, "rfp", bid.getRfp());
        putIfNotNull(d, BID_HOTEL_RFP_TYPE, bid.getType());
        putIfNotNull(d, "questionnaire", bid.getQuestionnaire());
        putIfNotNull(d, "subject", bid.getSubject());
        putIfNotNull(d, "buyer", bid.getBuyer());
        putIfNotNull(d, "supplier", bid.getSupplier());
        putIfNotNull(d, "analytics", bid.getAnalytics());
        putIfNotNull(d, "created", bid.getCreationDetails());
        putIfNotNull(d, "state", bid.getState());
        putIfNotNull(d, "negotiations", bid.getNegotiations());
        putIfNotNull(d, "offer", bid.getOffer());
        // "rateLoadingInformation" not saved as part of the BID
        putIfNotNull(d, "finalAgreementDate", bid.getFinalAgreementDate());
    }
}
