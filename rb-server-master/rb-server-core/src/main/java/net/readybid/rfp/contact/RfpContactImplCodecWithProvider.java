package net.readybid.rfp.contact;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.company.RfpCompanyImpl;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class RfpContactImplCodecWithProvider extends RbMongoCodecWithProvider<RfpContactImpl> {

    public RfpContactImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpContactImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RfpContactImpl newInstance() {
        return new RfpContactImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpContactImpl>> decodeMap) {
        decodeMap.put("id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("fullName", (c, reader, context) -> c.setFullName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("jobTitle", (c, reader, context) -> c.setJobTitle(reader.readString()));
        decodeMap.put("company", (c, reader, context) -> c.setCompany(read(reader, context, RfpCompanyImpl.class)));
        decodeMap.put("isUser", (c, reader, context) -> c.setIsUser(reader.readBoolean()));
    }

    @Override
    public void encodeDocument(Document d, RfpContactImpl contact, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "id", contact.getId());
        putIfNotNull(d, "firstName", contact.getFirstName());
        putIfNotNull(d, "lastName", contact.getLastName());
        putIfNotNull(d, "fullName", contact.getFullName());
        putIfNotNull(d, "emailAddress", contact.getEmailAddress());
        putIfNotNull(d, "phone", contact.getPhone());
        putIfNotNull(d, "jobTitle", contact.getJobTitle());
        putIfNotNull(d, "company", contact.getCompany());
        putIfNotNull(d, "isUser", contact.isUser());
    }
}
