package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfphotel.bid.core.*;
import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class HotelRfpBidSimpleStateCodec extends RbMongoCodecWithProvider<HotelRfpBidSimpleState> {

    public HotelRfpBidSimpleStateCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpBidSimpleState.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpBidSimpleState newInstance(){
        return new HotelRfpBidSimpleState();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpBidSimpleState>> decodeMap) {
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, HotelRfpBidStateStatus.class)));
        decodeMap.put("at", (c, reader, context) -> c.setAt(readDate(reader)));
        decodeMap.put("by", (c, reader, context) -> c.setBy(read(reader, context, BasicUserDetailsImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpBidSimpleState state, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "at", state.getAt());
        putIfNotNull(d, "status", state.getStatus());

        final BasicUserDetails by = state.getBy();
        if(by != null) d.put("by", by.getId());
    }
}
