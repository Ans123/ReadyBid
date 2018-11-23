package net.readybid.auth.useraccount.db;

import net.readybid.auth.account.core.AccountImpl;
import net.readybid.auth.user.UserImpl;
import net.readybid.auth.useraccount.UserAccountStatusDetails;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class UserAccountImplCodecWithProvider extends RbMongoCodecWithProvider<UserAccountImpl> {

    public UserAccountImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(UserAccountImpl.class, bsonTypeClassMap);
    }

    @Override
    protected UserAccountImpl newInstance() {
        return new UserAccountImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<UserAccountImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("user", (c, reader, context) -> c.setUser(read(reader, context, UserImpl.class)));
        decodeMap.put("account", (c, reader, context) -> c.setAccount(read(reader, context, AccountImpl.class)));
        decodeMap.put("jobTitle", (c, reader, context) -> c.setJobTitle(reader.readString()));
        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, UserAccountStatusDetails.class)));
        decodeMap.put("changed", (c, reader, context) -> c.setChanged(readLongObject(reader)));
        decodeMap.put("defaultBmView", (c, reader, context) -> c.setDefaultBidManagerView(reader.readObjectId()));
        decodeMap.put("lastBmView", (c, reader, context) -> c.setLastUsedBidManager(reader.readObjectId()));
    }

    @Override
    public void encodeDocument(Document d, UserAccountImpl userAccount, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", userAccount.getId());
        putIfNotNull(d, "userId", userAccount.getUserId());
        putIfNotNull(d, "accountId", userAccount.getAccountId());
        putIfNotNull(d, "jobTitle", userAccount.getJobTitle());
        putIfNotNull(d, "created", userAccount.getCreated());
        putIfNotNull(d, "status", userAccount.getUserAccountStatus());
        putIfNotNull(d, "changed", userAccount.getLastChangeTimestamp());
        putIfNotNull(d, "defaultBmView", userAccount.getDefaultBidManagerView());
        putIfNotNull(d, "lastBmView", userAccount.getLastUsedBidManagerView());
    }
}
