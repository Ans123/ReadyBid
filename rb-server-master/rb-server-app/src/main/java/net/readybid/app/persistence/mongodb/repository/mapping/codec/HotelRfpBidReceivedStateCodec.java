package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfphotel.bid.core.HotelRfpBidReceivedState;
import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class HotelRfpBidReceivedStateCodec extends RbMongoCodecWithProvider<HotelRfpBidReceivedState> {

    public HotelRfpBidReceivedStateCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidReceivedState.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidReceivedState newInstance(){
        return new HotelRfpBidReceivedState();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidReceivedState>> decodeMap) {
        decodeMap.put("buyerAt", (c, reader, context) -> c.setBuyerAt(readDate(reader)));
        decodeMap.put("buyerBy", (c, reader, context) -> c.setBuyerBy(read(reader, context, BasicUserDetailsImpl.class)));
        decodeMap.put("supplierAt", (c, reader, context) -> c.setSupplierAt(readDate(reader)));
        decodeMap.put("supplierBy", (c, reader, context) -> c.setSupplierBy(read(reader, context, BasicUserDetailsImpl.class)));
        decodeMap.put("errorsCount", (c, reader, context) -> c.setErrorsCount(readLong(reader)));
        decodeMap.put("touched", (c, reader, context) -> c.setResponseTouched(reader.readBoolean()));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpBidReceivedState state, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "status", state.getStatus());
        putIfNotNull(d, "buyerAt", state.getBuyerAt());

        BasicUserDetails by = state.getBuyerBy();
        if(by != null) d.put("buyerBy", by.getId());
        putIfNotNull(d, "supplierAt", state.getSupplierAt());

        by = state.getSupplierBy();
        if(by != null) d.put("supplierBy", by.getId());
        d.put("errorsCount", state.getErrorsCount());
        d.put("touched", state.isResponseTouched());
    }
}
