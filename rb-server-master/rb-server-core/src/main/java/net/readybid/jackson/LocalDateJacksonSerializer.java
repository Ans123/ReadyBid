package net.readybid.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by DejanK on 2/12/2017.
 *
 */
public class LocalDateJacksonSerializer extends StdSerializer<LocalDate> {

    public LocalDateJacksonSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(localDate == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(localDate.toString());
        }
    }
}
