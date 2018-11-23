package net.readybid.entities.hotel.db;

import net.readybid.entities.chain.HotelChainImpl;
import net.readybid.entities.core.db.AbstractEntityRbMongoCodecWithProvider;
import net.readybid.entities.hotel.core.HotelCategoryImpl;
import net.readybid.entities.hotel.core.HotelImpl;
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
public class HotelImplCodecWithProvider extends AbstractEntityRbMongoCodecWithProvider<HotelImpl> {

    public HotelImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelImpl.class, bsonTypeClassMap);
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelImpl>> decodeMap){
        super.decodeMapSetup(decodeMap);
        decodeMap.put("chain", (c, reader, context) -> c.setChain(read(reader, context, HotelChainImpl.class)));
        decodeMap.put("category", (c, reader, context) -> c.setCategory(read(reader, context, HotelCategoryImpl.class)));
        decodeMap.put("rating", (c, reader, context) -> c.setRating(readInt(reader)));
        decodeMap.put("amenities", (c, reader, context) -> c.setAmenities(readList(reader, String.class)));
        decodeMap.put("answers", (c, reader, context) -> c.setAnswers(readMapStringString(reader)));
    }

    @Override
    public void encodeDocument(Document d, HotelImpl hotel, BsonWriter bsonWriter, EncoderContext encoderContext) {
        super.encodeDocument(d, hotel, bsonWriter, encoderContext);
        putIfNotNull(d, "chain", hotel.getChain());
        putIfNotNull(d, "category", hotel.getCategory());
        putIfNotNull(d, "rating", hotel.getRating());
        putIfNotNull(d, "amenities", hotel.getAmenities());
        putIfNotNull(d, "answers", hotel.getAnswers());
    }

    @Override
    protected HotelImpl newInstance() {
        return new HotelImpl();
    }
}
