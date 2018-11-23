package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.core.entities.rfp.QuestionnaireResponseImpl;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl;
import net.readybid.entities.Id;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.codecs.BsonTypeClassMap;

import java.util.Map;

public class HotelRfpBidSetSupplierContactAndSendBidCommandProducerImplDecoder extends RbMongoCodecWithProvider<HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl> {

    public HotelRfpBidSetSupplierContactAndSendBidCommandProducerImplDecoder(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl newInstance() {
        return new HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setBidId(read(reader, context, Id.class)));
        decodeMap.put("bidType", (c, reader, context) -> c.setCurrentBidType(read(reader, context, HotelRfpType.class)));
        decodeMap.put("bidStatus", (c, reader, context) -> c.setBidStatus(read(reader, context, HotelRfpBidStateStatus.class)));
        decodeMap.put("supplierContact", (c, reader, context) -> c.setCurrentSupplierContact(read(reader, context, HotelRfpSupplierContact.class)));
        decodeMap.put("rfpId", (c, reader, context) -> c.setRfpId(read(reader, context, Id.class)));
        decodeMap.put("rfpName", (c, reader, context) -> c.setRfpName(reader.readString()));
        decodeMap.put("rfpChainSupport", (c, reader, context) -> c.setRfpChainSupport(reader.readBoolean()));
        decodeMap.put("coverLetterTemplate", (c, reader, context) -> c.setCoverLetterTemplate(reader.readString()));
        decodeMap.put("namCoverLetterTemplate", (c, reader, context) -> c.setNamCoverLetterTemplate(reader.readString()));
        decodeMap.put("defaultResponse", (c, reader, context) -> c.setDefaultResponse(read(reader, context, QuestionnaireResponseImpl.class)));
    }
}
