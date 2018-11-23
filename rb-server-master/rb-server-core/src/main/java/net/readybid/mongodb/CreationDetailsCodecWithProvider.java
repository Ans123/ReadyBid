package net.readybid.mongodb;

import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class CreationDetailsCodecWithProvider extends RbMongoCodecWithProvider<CreationDetails> {

    public CreationDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(CreationDetails.class, bsonTypeClassMap);
    }

    @Override
    protected CreationDetails newInstance() {
        return new CreationDetails();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<CreationDetails>> decodeMap) {
        decodeMap.put("at", (c, reader, context) -> c.setAt(readDate(reader)));
        decodeMap.put("by", (c, reader, context) -> c.setBy(read(reader, context, BasicUserDetailsImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, CreationDetails creationDetails, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "at", creationDetails.getAt());
        final BasicUserDetails by = creationDetails.getBy();
        if(by != null) d.put("by", by.getId());
    }
}
