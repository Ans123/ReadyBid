package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.codecs.DecoderContext;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
public interface RbMongoDecoder<T> {
   void decode(T t, BsonReader reader, DecoderContext context);
}
