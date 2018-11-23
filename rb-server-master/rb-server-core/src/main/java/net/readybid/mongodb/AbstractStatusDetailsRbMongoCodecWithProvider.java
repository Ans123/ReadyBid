package net.readybid.mongodb;

import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import net.readybid.utils.StatusDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public abstract class AbstractStatusDetailsRbMongoCodecWithProvider<S, T extends StatusDetails<S>> extends RbMongoCodecWithProvider<T> {

    private Class<S> sClass;

    public AbstractStatusDetailsRbMongoCodecWithProvider(BsonTypeClassMap bsonTypeClassMap, Class<T> tClass, Class<S> sClass) {
        super(tClass, bsonTypeClassMap);
        this.sClass = sClass;
    }

    public void decodeMapSetup(Map<String, RbMongoDecoder<T>> decodeMap){
        decodeMap.put("value", (c, reader, context) -> c.setValue(read(reader, context, sClass)));
        decodeMap.put("at", (c, reader, context) -> c.setAt(readDate(reader)));
        decodeMap.put("by", (c, reader, context) -> c.setBy(read(reader, context, BasicUserDetailsImpl.class)));
    }

    public void encodeDocument(Document d, T t, BsonWriter bsonWriter, EncoderContext encoderContext){
        putIfNotNull(d, "at", t.getAt());
        putIfNotNull(d, "value", t.getValue());

        final BasicUserDetails by = t.getBy();
        if(by != null) d.put("by", by.getId());
    }
}