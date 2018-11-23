package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class EnumCodec<E extends Enum<E>> implements Codec<E> {

    private final Class<E> eClass;

    public EnumCodec(Class<E> eClass) {
        this.eClass = eClass;
    }

    @Override
    public E decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final String s = bsonReader.readString();
        return s != null ? Enum.valueOf(eClass, s) : null;
    }

    @Override
    public void encode(BsonWriter bsonWriter, E e, EncoderContext encoderContext) {
        if(e != null){
            bsonWriter.writeString(e.toString());
        }
    }

    @Override
    public Class<E> getEncoderClass() {
        return eClass;
    }
}
