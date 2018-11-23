package net.readybid.entities.chain;

import net.readybid.entities.core.db.AbstractEntityRbMongoCodecWithProvider;
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
public class HotelChainImplCodecWithProvider extends AbstractEntityRbMongoCodecWithProvider<HotelChainImpl> {

    public HotelChainImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelChainImpl.class, bsonTypeClassMap);
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelChainImpl>> decodeMap){
        super.decodeMapSetup(decodeMap);
        decodeMap.put("code", (c, reader, context) -> c.setCode(reader.readString()));
        decodeMap.put("subtype", (c, reader, context) -> c.setSubtype(read(reader, context, HotelChainType.class)));
        decodeMap.put("master", (c, reader, context) -> c.setMasterChain(read(reader, context, HotelChainImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, HotelChainImpl hotel, BsonWriter bsonWriter, EncoderContext encoderContext) {
        super.encodeDocument(d, hotel, bsonWriter, encoderContext);
        putIfNotNull(d, "code", hotel.getCode());
        putIfNotNull(d, "subtype", hotel.getSubtype());
        putIfNotNull(d, "master", hotel.getMasterChain());
    }

    @Override
    protected HotelChainImpl newInstance() {
        return new HotelChainImpl();
    }
}
