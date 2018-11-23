package net.readybid.entities.hotel.db;

import net.readybid.entities.hotel.core.HotelCategoryImpl;
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
public class HotelCategoryImplCodecWithProvider extends RbMongoCodecWithProvider<HotelCategoryImpl> {

    public HotelCategoryImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelCategoryImpl.class, bsonTypeClassMap);
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelCategoryImpl>> decodeMap){
        decodeMap.put("id", (c, reader, context) -> c.setId(readInt(reader)));
        decodeMap.put("label", (c, reader, context) -> c.setLabel(reader.readString()));
    }

    @Override
    public void encodeDocument(Document d, HotelCategoryImpl category, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "id", category.getId());
        putIfNotNull(d, "label", category.getLabel());
    }

    @Override
    protected HotelCategoryImpl newInstance() {
        return new HotelCategoryImpl();
    }
}
