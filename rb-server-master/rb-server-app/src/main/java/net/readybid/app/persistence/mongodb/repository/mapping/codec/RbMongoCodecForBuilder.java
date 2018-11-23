package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.IBuilder;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonReader;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class RbMongoCodecForBuilder<T, S extends IBuilder<? extends T>> extends RbMongoCodecWithProvider<T> {

    private Map<String, RbMongoDecoder<S>> builderDecoderMap;

    protected RbMongoCodecForBuilder(Class<T> tClass, BsonTypeClassMap bsonTypeClassMap) {
        super(tClass, bsonTypeClassMap);
    }

    protected T decodeInstance(BsonReader reader, DecoderContext context){
        return decode(reader, context, getBuilderInstance(), getBuilderDecoderMap()).build();
    }

    private Map<String, RbMongoDecoder<S>> getBuilderDecoderMap() {
        if(builderDecoderMap == null) {
            final Map<String, RbMongoDecoder<S>> decoderMap = new HashMap<>();
            builderDecoderMapSetup(decoderMap);
            builderDecoderMap = Collections.unmodifiableMap(decoderMap);
        }
        return builderDecoderMap;
    }

    protected abstract void builderDecoderMapSetup(Map<String, RbMongoDecoder<S>> decoderMap);

    protected abstract S getBuilderInstance();
}
