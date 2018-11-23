package net.readybid.rfphotel.supplier;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.entities.chain.HotelChainImpl;
import net.readybid.entities.core.EntityImageImpl;
import net.readybid.entities.hotel.core.HotelCategoryImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class RfpHotelImplCodecWithProvider extends RbMongoCodecWithProvider<RfpHotelImpl> {

    public RfpHotelImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpHotelImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RfpHotelImpl newInstance() {
        return new RfpHotelImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpHotelImpl>> decodeMap) {
        decodeMap.put("entityId", (c, reader, context) -> c.setEntityId(reader.readObjectId()));
        decodeMap.put("accountId", (c, reader, context) -> c.setAccountId(reader.readObjectId()));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, EntityType.class)));
        decodeMap.put("industry", (c, reader, context) -> c.setIndustry(read(reader, context, EntityIndustry.class)));
        decodeMap.put("location", (c, reader, context) -> c.setLocation(read(reader, context, LocationImpl.class)));
        decodeMap.put("website", (c, reader, context) -> c.setWebsite(reader.readString()));
        decodeMap.put("logo", (c, reader, context) -> c.setLogo(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("chain", (c, reader, context) -> c.setChain(read(reader, context, HotelChainImpl.class)));
        decodeMap.put("category", (c, reader, context) -> c.setCategory(read(reader, context, HotelCategoryImpl.class)));
        decodeMap.put("rating", (c, reader, context) -> c.setRating(readInt(reader)));
        decodeMap.put("amenities", (c, reader, context) -> c.setAmenities(readList(reader, String.class)));
        decodeMap.put("image", (c, reader, context) -> c.setImage(read(reader, context, EntityImageImpl.class)));

    }

    @Override
    public void encodeDocument(Document d, RfpHotelImpl hotel, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "entityId", hotel.getEntityId());
        putIfNotNull(d, "accountId", hotel.getAccountId());
        putIfNotNull(d, "name", hotel.getName());
        putIfNotNull(d, "type", hotel.getType());
        putIfNotNull(d, "industry", hotel.getIndustry());
        putIfNotNull(d, "location", hotel.getLocation());
        putIfNotNull(d, "website", hotel.getWebsite());
        putIfNotNull(d, "logo", hotel.getLogo());
        putIfNotNull(d, "emailAddress", hotel.getEmailAddress());
        putIfNotNull(d, "phone", hotel.getPhone());
        putIfNotNull(d, "chain", hotel.getChain());
        putIfNotNull(d, "category", hotel.getCategory());
        putIfNotNull(d, "rating", hotel.getRating());
        putIfNotNull(d, "amenities", hotel.getAmenities());
        putIfNotNull(d, "image", hotel.getImage());
    }
}
