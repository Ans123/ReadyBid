package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.LocalDate;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class LocalDateCodec implements Codec<LocalDate> {

    @Override
    public LocalDate decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final String[] dateString = bsonReader.readString().split("-");

        if(dateString.length == 3){
            return LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
        } else {
            return null;
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, LocalDate localDate, EncoderContext encoderContext) {
        if(localDate != null) bsonWriter.writeString(localDate.toString());
    }

    @Override
    public Class<LocalDate> getEncoderClass() {
        return LocalDate.class;
    }
}
