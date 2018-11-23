package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.app.core.entities.entity.EntityType;
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
public class NegotiatorImplCodecWithProvider extends RbMongoCodecWithProvider<NegotiatorImpl> {

    public NegotiatorImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(NegotiatorImpl.class, bsonTypeClassMap);
    }

    @Override
    protected NegotiatorImpl newInstance() {
        return new NegotiatorImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<NegotiatorImpl>> decodeMap) {

        decodeMap.put("userId", (c, reader, context) -> c.setUserId(reader.readObjectId()));
        decodeMap.put("userAccountId", (c, reader, context) -> c.setUserAccountId(reader.readObjectId()));
        decodeMap.put("accountId", (c, reader, context) -> c.setAccountId(reader.readObjectId()));
        decodeMap.put("accountType", (c, reader, context) -> c.setAccountType(read(reader, context, EntityType.class)));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, BidManagerViewSide.class)));
        decodeMap.put("logo", (c, reader, context) -> c.setAccountLogo(reader.readString()));
        decodeMap.put("companyName", (c, reader, context) -> c.setCompanyName(reader.readString()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("jobTitle", (c, reader, context) -> c.setJobTitle(reader.readString()));
        decodeMap.put("profilePicture", (c, reader, context) -> c.setProfilePicture(reader.readString()));
    }

    @Override
    public void encodeDocument(Document document, NegotiatorImpl tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(document, "userAccountId", tObject.getUserAccountId());
        putIfNotNull(document, "type", tObject.getType());
    }
}