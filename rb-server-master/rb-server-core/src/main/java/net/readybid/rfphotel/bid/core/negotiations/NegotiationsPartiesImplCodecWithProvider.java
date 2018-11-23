package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 7/25/2017.
 */
public class NegotiationsPartiesImplCodecWithProvider extends RbMongoCodecWithProvider<NegotiationsPartiesImpl> {

    public NegotiationsPartiesImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiationsPartiesImpl.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiationsPartiesImpl newInstance() {
        return new NegotiationsPartiesImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<NegotiationsPartiesImpl>> decodeMap) {
        decodeMap.put("buyer", (c, reader, context) -> c.setBuyer(read(reader, context, NegotiatorImpl.class)));
        decodeMap.put("supplier", (c, reader, context) -> c.setSupplier(read(reader, context, NegotiatorImpl.class)));
    }

    @Override
    public void encodeDocument(Document document, NegotiationsPartiesImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "buyer", tObject.getBuyer());
        putIfNotNull(document, "supplier", tObject.getSupplier());
    }
}