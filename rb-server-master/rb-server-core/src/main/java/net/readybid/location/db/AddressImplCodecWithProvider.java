package net.readybid.location.db;

import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.address.Country;
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
public class AddressImplCodecWithProvider extends RbMongoCodecWithProvider<AddressImpl> {

    public AddressImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(AddressImpl.class, bsonTypeClassMap);
    }

    @Override
    protected AddressImpl newInstance() {
        return new AddressImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<AddressImpl>> decodeMap) {
        decodeMap.put("address1", (a, reader, context) -> a.setAddress1(reader.readString()));
        decodeMap.put("address2", (a, reader, context) -> a.setAddress2(reader.readString()));
        decodeMap.put("city", (a, reader, context) -> a.setCity(reader.readString()));
        decodeMap.put("county", (a, reader, context) -> a.setCounty(reader.readString()));
        decodeMap.put("state", (a, reader, context) -> a.setState(reader.readString()));
        decodeMap.put("region", (a, reader, context) -> a.setRegion(reader.readString()));
        decodeMap.put("zip", (a, reader, context) -> a.setZip(reader.readString()));
        decodeMap.put("country", (a, reader, context) -> a.setCountry(read(reader, context, Country.class)));
    }

    @Override
    public void encodeDocument(Document d, AddressImpl address, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "address1", address.getAddress1());
        putIfNotNull(d, "address2", address.getAddress2());
        putIfNotNull(d, "city", address.getCity());
        putIfNotNull(d, "county", address.getCounty());
        putIfNotNull(d, "state", address.getState());
        putIfNotNull(d, "region", address.getRegion());
        putIfNotNull(d, "zip", address.getZip());
        putIfNotNull(d, "country", address.getCountry());
    }
}
