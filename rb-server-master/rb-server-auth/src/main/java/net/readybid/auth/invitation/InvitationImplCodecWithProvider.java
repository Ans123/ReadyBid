package net.readybid.auth.invitation;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
public class InvitationImplCodecWithProvider extends RbMongoCodecWithProvider<InvitationImpl> {

    public InvitationImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(InvitationImpl.class, bsonTypeClassMap);
    }

    @Override
    protected InvitationImpl newInstance() {
        return new InvitationImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<InvitationImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("jobTitle", (c, reader, context) -> c.setJobTitle(reader.readString()));
        decodeMap.put("accountName", (c, reader, context) -> c.setAccountName(reader.readString()));
        decodeMap.put("accountId", (c, reader, context) -> c.setAccountId(reader.readObjectId()));
        decodeMap.put("token", (c, reader, context) -> c.setToken(reader.readString()));
        decodeMap.put("targetId", (c, reader, context) -> c.setTargetId(reader.readObjectId()));
        decodeMap.put("expiryDate", (c, reader, context) -> c.setExpiryDate(readDate(reader)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, InvitationStatusDetails.class)));
        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
    }

    @Override
    public void encodeDocument(Document d, InvitationImpl invitation, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", invitation.getId());
        putIfNotNull(d, "firstName", invitation.getFirstName());
        putIfNotNull(d, "lastName", invitation.getLastName());
        putIfNotNull(d, "emailAddress", invitation.getEmailAddress());
        putIfNotNull(d, "phone", invitation.getPhone());
        putIfNotNull(d, "jobTitle", invitation.getJobTitle());
        putIfNotNull(d, "accountName", invitation.getAccountName());
        putIfNotNull(d, "accountId", invitation.getAccountId());
        putIfNotNull(d, "token", invitation.getToken());
        putIfNotNull(d, "targetId", invitation.getTargetId());
        putIfNotNull(d, "expiryDate", invitation.getExpiryDate());
        putIfNotNull(d, "status", invitation.getStatus());
        putIfNotNull(d, "created", invitation.getCreated());
    }
}
