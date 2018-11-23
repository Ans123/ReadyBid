package net.readybid.location.db;

import net.readybid.app.core.entities.location.address.Country;
import net.readybid.location.AddressView;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class AddressViewCodecWithProvider extends RbMongoCodecWithProvider<AddressView> {

    public AddressViewCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(AddressView.class, bsonTypeClassMap);
    }

    @Override
    protected AddressView newInstance() {
        return new AddressView();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<AddressView>> decodeMap) {
        decodeMap.put("address1", (a, reader, context) -> a.address1 = reader.readString());
        decodeMap.put("address2", (a, reader, context) -> a.address2 = reader.readString());
        decodeMap.put("city", (a, reader, context) -> a.city = reader.readString());
        decodeMap.put("county", (a, reader, context) -> a.county = reader.readString());
        decodeMap.put("state", (a, reader, context) -> a.state = reader.readString());
        decodeMap.put("region", (a, reader, context) -> a.region = reader.readString());
        decodeMap.put("zip", (a, reader, context) -> a.zip = reader.readString());
        decodeMap.put("country", (a, reader, context) -> a.country = read(reader, context, Country.class));
    }

    @Override
    public void encodeDocument(Document d, AddressView address, BsonWriter bsonWriter, EncoderContext encoderContext) {
    }
}
