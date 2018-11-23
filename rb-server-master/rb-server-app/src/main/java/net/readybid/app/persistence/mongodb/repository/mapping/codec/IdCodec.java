package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.entities.Id;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 7/20/2018.
 *
 */
public class IdCodec implements Codec<Id> {

    @Override
    public Id decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return Id.valueOf(bsonReader.readObjectId());
    }

    @Override
    public void encode(BsonWriter bsonWriter, Id id, EncoderContext encoderContext) {
        if(id != null && id.value != null && ObjectId.isValid(id.value)) bsonWriter.writeObjectId(new ObjectId(id.value));
    }

    @Override
    public Class<Id> getEncoderClass() {
        return Id.class;
    }
}
