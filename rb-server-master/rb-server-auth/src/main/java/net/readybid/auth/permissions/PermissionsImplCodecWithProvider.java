package net.readybid.auth.permissions;

import net.readybid.auth.permission.Permission;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.Map;

import static org.bson.BsonType.END_OF_DOCUMENT;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class PermissionsImplCodecWithProvider extends RbMongoCodecWithProvider<PermissionsImpl> {

    public PermissionsImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(PermissionsImpl.class, bsonTypeClassMap);
    }

    @Override
    protected PermissionsImpl newInstance() {
        return new PermissionsImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<PermissionsImpl>> decodeMap) {}

    @Override
    public PermissionsImpl decode(BsonReader reader, DecoderContext context) {
        final PermissionsImpl permissions = newInstance();

        reader.readStartDocument();
        while (reader.readBsonType() != END_OF_DOCUMENT) {
            final String fieldName = reader.readName();
            final BsonType bsonType = reader.getCurrentBsonType();
            if(bsonType == null){
                reader.readNull();
            } else {
                permissions.put(fieldName, readEnumSet(reader, Permission.class));
            }
        }
        reader.readEndDocument();

        return permissions;
    }


    @Override
    public void encodeDocument(Document d, PermissionsImpl permissions, BsonWriter bsonWriter, EncoderContext encoderContext) {
        System.out.println("Encode Permissions");
        if(permissions != null){
            for(String id : permissions.keySet()){
                d.put(id, permissions.get(id));
            }
        }
    }
}
