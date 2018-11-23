package net.readybid.rfp.buyer;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.company.RfpCompanyImpl;
import net.readybid.rfp.contact.RfpContactImpl;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class BuyerImplCodecWithProvider extends RbMongoCodecWithProvider<BuyerImpl> {

    public BuyerImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(BuyerImpl.class, bsonTypeClassMap);
    }

    @Override
    protected BuyerImpl newInstance() {
        return new BuyerImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<BuyerImpl>> decodeMap) {
        decodeMap.put("company", (c, reader, context) -> c.setCompany(read(reader, context, RfpCompanyImpl.class)));
        decodeMap.put("contact", (c, reader, context) -> c.setContact(read(reader, context, RfpContactImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, BuyerImpl buyer, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "company", buyer.getCompany());
        putIfNotNull(d, "contact", buyer.getContact());
    }
}
