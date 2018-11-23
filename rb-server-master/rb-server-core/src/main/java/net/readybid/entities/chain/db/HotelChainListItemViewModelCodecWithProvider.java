package net.readybid.entities.chain.db;

import net.readybid.entities.chain.HotelChainListItemViewModel;
import net.readybid.entities.chain.HotelChainType;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
public class HotelChainListItemViewModelCodecWithProvider extends RbMongoCodecWithProvider<HotelChainListItemViewModel> {

    public HotelChainListItemViewModelCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelChainListItemViewModel.class, bsonTypeClassMap);
    }

    @Override
    protected HotelChainListItemViewModel newInstance() {
        return new HotelChainListItemViewModel();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelChainListItemViewModel>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.id = reader.readObjectId().toString());
        decodeMap.put("code", (c, reader, context) -> c.code = reader.readString());
        decodeMap.put("name", (c, reader, context) -> c.name = reader.readString());
        decodeMap.put("subtype", (c, reader, context) -> c.subtype = read(reader, context, HotelChainType.class));
        decodeMap.put("master", (c, reader, context) -> c.masterChain = read(reader, context, HotelChainListItemViewModel.class));
    }

    @Override
    public void encodeDocument(Document d, HotelChainListItemViewModel tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
    }
}
