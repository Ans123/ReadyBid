package net.readybid.auth.account.db;

import net.readybid.auth.account.core.AccountImpl;
import net.readybid.auth.account.core.AccountStatusDetails;
import net.readybid.auth.permissions.PermissionsImpl;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
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
public class AccountImplCodecWithProvider extends RbMongoCodecWithProvider<AccountImpl> {

    public AccountImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(AccountImpl.class, bsonTypeClassMap);
    }

    @Override
    protected AccountImpl newInstance() {
        return new AccountImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<AccountImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("entityId", (c, reader, context) -> c.setEntityId(reader.readObjectId()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, EntityType.class)));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("industry", (c, reader, context) -> c.setIndustry(read(reader, context, EntityIndustry.class)));
        decodeMap.put("location", (c, reader, context) -> c.setLocation(read(reader, context, LocationImpl.class)));
        decodeMap.put("website", (c, reader, context) -> c.setWebsite(reader.readString()));
        decodeMap.put("logo", (c, reader, context) -> c.setLogo(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("permissions", (c, reader, context) -> c.setPermissions(read(reader, context, PermissionsImpl.class)));
        decodeMap.put("primaryRepresentativeUserAccountId", (c, reader, context) -> c.setPrimaryRepresentativeUserAccountId(reader.readObjectId()));
        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, AccountStatusDetails.class)));
        decodeMap.put("changed", (c, reader, context) -> c.setLastChanged(readLongObject(reader)));
    }

    @Override
    public void encodeDocument(Document d, AccountImpl account, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", account.getId());
        putIfNotNull(d, "entityId", account.getEntityId());
        putIfNotNull(d, "type", account.getType());
        putIfNotNull(d, "name", account.getName());
        putIfNotNull(d, "industry", account.getIndustry());
        putIfNotNull(d, "location", account.getLocation());
        putIfNotNull(d, "website", account.getWebsite());
        putIfNotNull(d, "logo", account.getLogo());
        putIfNotNull(d, "emailAddress", account.getEmailAddress());
        putIfNotNull(d, "phone", account.getPhone());

//        final Permissions p = account.getPermissions();
//        if(p != null) {
//            final Document d1 = new Document();
//            RbMongoCodecWithProvider<PermissionsImpl> pc = (RbMongoCodecWithProvider<PermissionsImpl>) registry.get(PermissionsImpl.class);
//            pc.encode(bsonWriter, (PermissionsImpl) p, encoderContext);
//            d.put("permissions", d1);
//        }

        putIfNotNull(d, "permissions", account.getPermissions());
        putIfNotNull(d, "primaryRepresentativeUserAccountId", account.getPrimaryRepresentativeUserAccountId());
        putIfNotNull(d, "created", account.getCreated());
        putIfNotNull(d, "status", account.getStatus());
        putIfNotNull(d, "changed", account.getLastChangeTimestamp());
    }
}
