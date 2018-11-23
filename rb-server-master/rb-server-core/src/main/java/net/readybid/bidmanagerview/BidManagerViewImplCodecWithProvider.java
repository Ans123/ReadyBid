package net.readybid.bidmanagerview;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.type.RfpType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
public class BidManagerViewImplCodecWithProvider extends RbMongoCodecWithProvider<BidManagerViewImpl> {

    public BidManagerViewImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(BidManagerViewImpl.class, bsonTypeClassMap);
    }

    @Override
    protected BidManagerViewImpl newInstance() {
        return new BidManagerViewImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<BidManagerViewImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("rfpId", (c, reader, context) -> c.setRfpId(reader.readObjectId()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, BidManagerViewType.class)));
        decodeMap.put("rfpType", (c, reader, context) -> c.setRfpType(read(reader, context, RfpType.class)));
        decodeMap.put("side", (c, reader, context) -> c.setSide(read(reader, context, BidManagerViewSide.class)));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("columns", (c, reader, context) -> c.setColumns(readList(reader, String.class)));
        decodeMap.put("filter", (c, reader, context) -> c.setFilter(readMapWithInvalidKeys(readMap(reader))));
        decodeMap.put("sort", (c, reader, context) -> c.setSort(readMapWithInvalidKeys(readMap(reader))));
        decodeMap.put("group", (c, reader, context) -> c.setGroup(reader.readString()));
        decodeMap.put("ownerId", (c, reader, context) -> c.setOwner(reader.readObjectId()));
    }

    @Override
    public void encodeDocument(Document d, BidManagerViewImpl bid, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", bid.getId());
        putIfNotNull(d, "rfpId", bid.getRfpId());
        putIfNotNull(d, "type", bid.getType());
        putIfNotNull(d, "rfpType", bid.getRfpType());
        putIfNotNull(d, "side", bid.getSide());
        putIfNotNull(d, "name", bid.getName());
        putIfNotNull(d, "columns", bid.getColumns());
        putIfNotNull(d, "filter", writeMapWithInvalidKeys(bid.getFilter()));
        putIfNotNull(d, "sort", writeMapWithInvalidKeys(bid.getSort()));
        putIfNotNull(d, "group", bid.getGroup());
        putIfNotNull(d, "ownerId", bid.getOwner());
    }
}
