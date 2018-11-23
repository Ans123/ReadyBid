package net.readybid.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by DejanK on 2/10/2017.
 *
 */
public class LocalDateJacksonDeserializer extends StdDeserializer<LocalDate> {

    public LocalDateJacksonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            return LocalDate.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch(RuntimeException e){
            return null;
//            throw new BadRequestException(String.format("\"%s\" is not valid date. Please supply date in \"yyyy-MM-dd\" format.", stringDate));
        }
    }
}
