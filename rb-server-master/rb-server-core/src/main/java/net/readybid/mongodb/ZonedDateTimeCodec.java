package net.readybid.mongodb;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by DejanK on 5/14/2017.
 *
 */
public class ZonedDateTimeCodec implements Codec<ZonedDateTime> {

    @Override
    public ZonedDateTime decode(BsonReader bsonReader, DecoderContext decoderContext) {
        try {
            final String dateString = bsonReader.readString();
            return ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (RuntimeException e){
            return null;
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, ZonedDateTime zonedDateTime, EncoderContext encoderContext) {
        if(zonedDateTime != null) bsonWriter.writeString(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zonedDateTime));
    }

    @Override
    public Class<ZonedDateTime> getEncoderClass() {
        return ZonedDateTime.class;
    }
}
